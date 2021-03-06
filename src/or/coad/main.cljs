(ns or.coad.main
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require ["js-base64" :as base64]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [cljs.reader :as reader]
            [goog.string.format]  ;; required for release build
            [or.coad.blazon :as blazon]
            [or.coad.charge :as charge]
            [or.coad.config :as config]
            [or.coad.division :as division]
            [or.coad.filter :as filter]
            [or.coad.form :as form]
            [or.coad.hatching :as hatching]
            [or.coad.options :as options]
            [or.coad.ordinary :as ordinary]
            [or.coad.render :as render]
            [or.coad.tincture :as tincture]
            [or.coad.util :as util]
            [re-frame.core :as rf]
            [reagent.dom :as r]))

;; subs

(rf/reg-sub
 :get
 (fn [db [_ & args]]
   (get-in db args)))

(rf/reg-sub
 :get-in
 (fn [db [_ path]]
   (get-in db path)))

(rf/reg-sub
 :get-division-type
 (fn [db [_ path]]
   (let [division (get-in db (conj path :division :type))]
     (or division :none))))

(rf/reg-sub
 :load-data
 (fn [db [_ name]]
   (let [data (get-in db [:loaded-data name])]
     (cond
       (= data :loading) nil
       data              data
       :else             (do
                           (rf/dispatch-sync [:set :loaded-data name :loading])
                           (go
                             (->
                              (http/get name)
                              <!
                              :body
                              (as-> result
                                  (let [parsed (if (string? result)
                                                 (reader/read-string result)
                                                 result)]
                                    (rf/dispatch [:set :loaded-data name parsed])))))
                           nil)))))

;; events


(rf/reg-event-db
 :initialize-db
 (fn [db [_]]
   (merge {:render-options {:component :render-options
                            :mode      :colours
                            :outline?  false
                            :squiggly? false
                            :ui        {:selectable-fields? true}}
           :ui             {:component-open? {[:render-options] true}}
           :coat-of-arms   config/default-coat-of-arms} db)))

(rf/reg-event-db
 :set
 (fn [db [_ & args]]
   (assoc-in db (drop-last args) (last args))))

(rf/reg-event-db
 :set-in
 (fn [db [_ path value]]
   (assoc-in db path value)))

(rf/reg-event-db
 :remove-in
 (fn [db [_ path]]
   (let [parent-path (drop-last path)
         value       (last path)]
     (if (util/contains-in? db parent-path)
       (update-in db parent-path dissoc value)
       db))))

(rf/reg-event-db
 :toggle-in
 (fn [db [_ path]]
   (update-in db path not)))

(rf/reg-event-db
 :set-division-type
 (fn [db [_ path new-type]]
   (if (= new-type :none)
     (-> db
         (update-in path dissoc :division)
         (update-in (conj path :content) #(or % {:tincture :none})))
     (-> db
         (assoc-in (conj path :division :type) new-type)
         (update-in (conj path :division :line :type) #(or % :straight))
         (update-in (conj path :division :fields)
                    (fn [current-value]
                      (let [current                      (or current-value [])
                            current-type                 (get-in db (conj path :division :type))
                            current-mandatory-part-count (division/mandatory-part-count current-type)
                            new-mandatory-part-count     (division/mandatory-part-count new-type)
                            min-mandatory-part-count     (min current-mandatory-part-count
                                                              new-mandatory-part-count)
                            current                      (if (or (= current-mandatory-part-count
                                                                    new-mandatory-part-count)
                                                                 (<= (count current) min-mandatory-part-count))
                                                           current
                                                           (subvec current 0 min-mandatory-part-count))
                            default                      (division/default-fields new-type)]
                        (cond
                          (< (count current) (count default)) (into current (subvec default (count current)))
                          (> (count current) (count default)) (subvec current 0 (count default))
                          :else                               current))))
         (update-in (conj path :division) #(merge %
                                                  (options/sanitize-or-nil % (division/options %))))
         (update-in path dissoc :content)
         (cond->
             (not (division/counterchangable? {:type new-type})) (update-in (conj path :components)
                                                                            (fn [components]
                                                                              (->> components
                                                                                   (map #(update % :field dissoc :counterchanged?))
                                                                                   vec))))))))

(rf/reg-event-db
 :set-ordinary-type
 (fn [db [_ path new-type]]
   (-> db
       (assoc-in (conj path :type) new-type)
       (update-in path #(merge %
                               (options/sanitize-or-nil % (ordinary/options %)))))))
(rf/reg-event-db
 :set-charge-type
 (fn [db [_ path new-type]]
   (-> db
       (assoc-in (conj path :type) new-type)
       (update-in path #(merge %
                               (options/sanitize-or-nil % (charge/options %)))))))

(rf/reg-event-fx
 :add-component
 (fn [{:keys [db]} [_ path value]]
   (let [components-path (conj path :components)
         index           (count (get-in db components-path))]
     {:db (update-in db components-path #(-> %
                                             (conj value)
                                             vec))
      :fx [[:dispatch [:ui-submenu-open (conj components-path index (case (:component value)
                                                                      :ordinary "Select Ordinary"
                                                                      :charge   "Select Charge"))]]
           [:dispatch [:ui-component-open (conj components-path index)]]
           [:dispatch [:ui-component-open (conj components-path index :field)]]]})))

(rf/reg-event-db
 :remove-component
 (fn [db [_ path]]
   (let [components-path (drop-last path)
         index           (last path)]
     (update-in db components-path (fn [components]
                                     (vec (concat (subvec components 0 index)
                                                  (subvec components (inc index)))))))))

(rf/reg-event-db
 :move-component-up
 (fn [db [_ path]]
   (let [components-path (drop-last path)
         index           (last path)]
     (update-in db components-path (fn [components]
                                     (let [num-components (count components)]
                                       (if (>= index num-components)
                                         components
                                         (-> components
                                             (subvec 0 index)
                                             (conj (get components (inc index)))
                                             (conj (get components index))
                                             (concat (subvec components (+ index 2)))
                                             vec))))))))

(rf/reg-event-db
 :move-component-down
 (fn [db [_ path]]
   (let [components-path (drop-last path)
         index           (last path)]
     (update-in db components-path (fn [components]
                                     (if (zero? index)
                                       components
                                       (-> components
                                           (subvec 0 (dec index))
                                           (conj (get components index))
                                           (conj (get components (dec index)))
                                           (concat (subvec components (inc index)))
                                           vec)))))))

(rf/reg-event-db
 :update-charge
 (fn [db [_ path changes]]
   (update-in db path merge changes)))

;; views


(def defs
  (into
   [:defs
    filter/shadow
    filter/shiny
    filter/glow
    tincture/patterns
    [:pattern#void {:width         20
                    :height        20
                    :pattern-units "userSpaceOnUse"}
     [:rect {:x      0
             :y      0
             :width  20
             :height 20
             :fill   "#fff"}]
     [:rect {:x      0
             :y      0
             :width  10
             :height 10
             :fill   "#ddd"}]
     [:rect {:x      10
             :y      10
             :width  10
             :height 10
             :fill   "#ddd"}]]
    (let [spacing 2
          width   (* spacing 2)
          size    0.3]
      [:pattern {:id            "selected"
                 :width         width
                 :height        width
                 :pattern-units "userSpaceOnUse"}
       [:rect {:x      0
               :y      0
               :width  width
               :height width
               :fill   "#f5f5f5"}]
       [:g {:fill "#000"}
        [:circle {:cx 0
                  :cy 0
                  :r  size}]
        [:circle {:cx width
                  :cy 0
                  :r  size}]
        [:circle {:cx 0
                  :cy width
                  :r  size}]
        [:circle {:cx width
                  :cy width
                  :r  size}]
        [:circle {:cx spacing
                  :cy spacing
                  :r  size}]]])]))

(defn forms []
  [:<>
   [:div {:style {:display "inline-block"}}
    [form/form-render-options]]
   [:br]
   [:div {:style {:display        "inline-block"
                  :padding-bottom "20px"}}
    [:div.title "Coat of Arms"]
    [form/form-for-field [:coat-of-arms :field]]]])

(defn app []
  (fn []
    (let [coat-of-arms   @(rf/subscribe [:get :coat-of-arms])
          mode           @(rf/subscribe [:get :render-options :mode])
          render-options @(rf/subscribe [:get :render-options])
          state-base64   (.encode base64 (prn-str coat-of-arms))]
      (when coat-of-arms
        (js/history.replaceState nil nil (str "#" state-base64)))
      [:<>
       [:div {:style    {:width    "100%"
                         :height   "calc(100vh - 4em)"
                         :top      "4em"
                         :position "relative"
                         :padding  "10px"}
              :on-click #(do (rf/dispatch [:ui-component-deselect-all])
                             (rf/dispatch [:ui-submenu-close-all])
                             (.stopPropagation %))}
        [:svg {:id                  "svg"
               :style               {:width    "25em"
                                     :height   "32em"
                                     :position "absolute"}
               :viewBox             "0 0 520 1000"
               :preserveAspectRatio "xMidYMin slice"}
         defs
         (when (= mode :hatching)
           [:defs
            hatching/patterns])
         [:g {:filter "url(#shadow)"}
          [:g {:transform "translate(10,10) scale(5,5)"}
           [render/coat-of-arms coat-of-arms render-options :db-path [:coat-of-arms]]]]]
        [:div.blazonry {:style {:position "absolute"
                                :left     10
                                :top      "34em"
                                :width    "calc(25em - 20px)"
                                :padding  10
                                :border   "1px solid #ddd"}}
         [:span.disclaimer "Blazon (very rudimentary, very beta)"]
         [:div.blazon
          (blazon/encode-field (:field coat-of-arms) :root? true)]]
        [:div {:style {:position       "absolute"
                       :left           "27em"
                       :width          "calc(100vw - 27em)"
                       :height         "calc(100vh - 4em)"
                       :overflow       "auto"
                       :padding-top    "5px"
                       :padding-bottom "1em"}}
         [forms]]]
       [:div.credits
        [:a {:href   "https://github.com/or/coad/"
             :target "_blank"} "Code and resource attribution on " [:i.fab.fa-github] " github:or/coad"]]])))

(defn stop []
  (println "Stopping..."))

(defn start []
  (rf/dispatch-sync [:initialize-db])
  (when (not goog.DEBUG)
    (let [hash (subs js/location.hash 1)]
      (when (> (count hash) 0)
        (let [data (->>
                    hash
                    (.decode base64)
                    reader/read-string)]
          (rf/dispatch-sync [:set :coat-of-arms data])))))
  (r/render [app]
            (.getElementById js/document "app")))

(defn ^:export init []
  (start))
