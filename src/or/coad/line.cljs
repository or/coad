(ns or.coad.line
  (:require ["svgpath" :as svgpath]
            [clojure.string :as s]
            [clojure.walk :as walk]
            [or.coad.catmullrom :as catmullrom]
            [or.coad.options :as options]
            [or.coad.random :as random]
            [or.coad.svg :as svg]
            [or.coad.vector :as v]))

(declare options)

(defn line-with-offset [length offset pattern-width pattern]
  (let [offset-length (* offset pattern-width)
        repetitions   (-> length
                          (- offset-length)
                          (/ pattern-width)
                          Math/ceil
                          int
                          inc)]
    {:line   (-> [["l" offset-length 0]]
                 (into (->> pattern
                            (repeat repetitions)))
                 (->> (apply merge))
                 (conj ["l" offset-length 0])
                 vec)
     :offset (min 0 offset-length)
     :length (-> (* repetitions pattern-width)
                 (+ (* offset-length 2)))}))

(defn straight
  {:display-name "Straight"}
  [_ length _]
  {:line   ["l" [length 0]]
   :start  0
   :length length})

(defn invected
  {:display-name "Invected"}
  [values length _]
  (let [{:keys [eccentricity
                offset
                width]} (options/sanitize values (options values))
        radius-x        (/ width 2)
        radius-y        (* radius-x eccentricity)]
    (line-with-offset
     length offset width
     ["a" radius-x radius-y 0 0 1 [width 0]])))

(defn engrailed
  {:display-name "Engrailed"}
  [values length _]
  (let [{:keys [eccentricity
                offset
                width]} (options/sanitize values (options values))
        radius-x        (/ width 2)
        radius-y        (* radius-x eccentricity)]
    (line-with-offset
     length offset width
     ["a" radius-x radius-y 0 0 0 [radius-x (- radius-y)]
      "a" radius-x radius-y 0 0 0 [radius-x radius-y]])))

(defn embattled
  {:display-name "Embattled"}
  [values length {:keys [reversed?]}]
  (let [{:keys [eccentricity
                offset
                width]} (options/sanitize values (options values))
        half-width      (/ width 2)
        height          (* eccentricity half-width)]
    (line-with-offset
     length offset width
     (if reversed?
       ["l"
        [half-width 0
         [0 (- height)]
         [half-width 0]
         [0 height]]]
       ["l"
        [0 (- height)]
        [half-width 0]
        [0 height]
        [half-width 0]]))))

(defn indented
  {:display-name "Indented"}
  [values length _]
  (let [{:keys [eccentricity
                offset
                width]} (options/sanitize values (options values))
        half-width      (/ width 2)
        height          (* eccentricity half-width)]
    (line-with-offset
     length offset width
     ["l"
      [half-width (- height)]
      [half-width height]])))

(defn dancetty
  {:display-name "Dancetty"}
  [values length {:keys [reversed?]}]
  (let [{:keys [eccentricity
                offset
                width]} (options/sanitize values (options values))
        half-width      (/ width 2)
        quarter-width   (/ width 4)
        half-height     (* quarter-width eccentricity)
        height          (* half-height 2)]
    (line-with-offset
     length offset width
     (if reversed?
       ["l"
        [quarter-width half-height]
        [half-width (- height)]
        [quarter-width half-height]]
       ["l"
        [quarter-width (- half-height)]
        [half-width height]
        [quarter-width (- half-height)]]))))

(defn wavy
  {:display-name "Wavy / undy"}
  [values length  {:keys [reversed?]}]
  (let [{:keys [eccentricity
                offset
                width]} (options/sanitize values (options values))
        half-width      (/ width 2)
        height          (* width eccentricity)]
    (line-with-offset
     length offset width
     ["a" half-width height 0 0 (if reversed? 0 1) [half-width 0]
      "a" half-width height 0 0 (if reversed? 1 0) [half-width 0]])))

(defn dovetailed
  {:display-name "Dovetailed"}
  [values length {:keys [reversed?]}]
  (let [{:keys [eccentricity
                offset
                width]} (options/sanitize values (options values))
        half-width      (/ width 2)
        third-width     (/ width 3)
        sixth-width     (/ width 6)
        height          (* half-width eccentricity)]
    (line-with-offset
     length offset width
     (if reversed?
       ["l"
        [(* third-width 2) 0]
        [(- sixth-width) (- height)]
        [third-width 0]
        [third-width 0]
        [(- sixth-width) height]]
       ["l"
        [(- sixth-width) (- height)]
        [third-width 0]
        [third-width 0]
        [(- sixth-width) height]
        [(* third-width 2) 0]]))))

(defn raguly
  {:display-name "Raguly"}
  [values length  {:keys [reversed?]}]
  (let [{:keys [eccentricity
                offset
                width]} (options/sanitize values (options values))
        half-width      (/ width 2)
        quarter-width   (/ width 4)
        height          (* half-width eccentricity)]
    (line-with-offset
     length offset width
     (if reversed?
       ["l"
        [quarter-width 0]
        [quarter-width (- height)]
        [half-width 0]
        [(- quarter-width) height]
        [quarter-width 0]]
       ["l"
        [quarter-width 0]
        [(- quarter-width) (- height)]
        [half-width 0]
        [quarter-width height]
        [quarter-width 0]]))))

(defn urdy
  {:display-name "Urdy"}
  [values length  {:keys [reversed?]}]
  (let [{:keys [eccentricity
                offset
                width]} (options/sanitize values (options values))
        quarter-width   (/ width 4)
        height          (* quarter-width eccentricity)
        half-height     (/ height 2)]
    (line-with-offset
     length offset width
     (if reversed?
       ["l"
        [0 half-height]
        [quarter-width height]
        [quarter-width (- height)]
        [0 (- height)]
        [quarter-width (- height)]
        [quarter-width height]
        [0 half-height]]
       ["l"
        [0 (- half-height)]
        [quarter-width (- height)]
        [quarter-width height]
        [0 height]
        [quarter-width height]
        [quarter-width (- height)]
        [0 (- half-height)]]))))

(def lines
  [#'straight
   #'invected
   #'engrailed
   #'embattled
   #'indented
   #'dancetty
   #'wavy
   #'dovetailed
   #'raguly
   #'urdy])

(def kinds-function-map
  (->> lines
       (map (fn [function]
              [(-> function meta :name keyword) function]))
       (into {})))

(def choices
  (->> lines
       (map (fn [function]
              [(-> function meta :display-name) (-> function meta :name keyword)]))))

(def default-options
  {:type         {:type    :choice
                  :choices choices
                  :default :straight}
   :eccentricity {:type    :range
                  :min     0.2
                  :max     3
                  :default 1}
   :width        {:type    :range
                  :min     2
                  :max     100
                  :default 10}
   :offset       {:type    :range
                  :min     -1
                  :max     3
                  :default 0}
   :flipped?     {:type    :boolean
                  :default false}})

(defn options [line]
  (options/merge
   default-options
   (get {:straight {:eccentricity nil
                    :offset       nil
                    :width        nil
                    :flipped?     nil}
         :dancetty {:width {:default 20}}
         :wavy     {:width {:default 20}}}
        (:type line))))

(defn jiggle [[previous
               {:keys [x y] :as current}
               _]]
  (let [dist          (-> current
                          (v/- previous)
                          (v/abs))
        jiggle-radius (/ dist 4)
        dx            (- (* (random/float) jiggle-radius)
                         jiggle-radius)
        dy            (- (* (random/float) jiggle-radius)
                         jiggle-radius)]
    {:x (+ x dx)
     :y (+ y dy)}))

(defn squiggly-path [path]
  (random/seed path)
  (let [points   (-> path
                     svg/new-path
                     (svg/points 100))
        points   (vec (concat [(first points)]
                              (map jiggle (partition 3 1 points))
                              [(last points)]))
        curve    (catmullrom/catmullrom points)
        new-path (catmullrom/curve->svg-path-relative curve)]
    new-path))

(defn squiggly-paths [data]
  (walk/postwalk #(cond-> %
                    (vector? %) ((fn [v]
                                   (if (= (first v) :d)
                                     [:d (squiggly-path (second v))]
                                     v))))
                 data))

(defn create [{:keys [type] :or {type :straight} :as line} length & {:keys [angle flipped? extra render-options] :or {extra 50} :as line-options}]
  (let [line-data           ((get kinds-function-map type)
                             line
                             (+ length extra) line-options)
        line-options-values (options/sanitize line (options line))
        line-flipped?       (:flipped? line-options-values)
        adjusted-path       (-> (:line line-data)
                                svg/make-path
                                (->>
                                 (str "M 0,0 "))
                                (cond->
                                    (:squiggly? render-options) squiggly-path))]
    (assoc line-data :line
           (-> adjusted-path
               svgpath
               (cond->
                   (or (and flipped? (not line-flipped?))
                       (and (not flipped?) line-flipped?)) (.scale 1 -1))
               (.rotate angle)
               .toString))))

(defn translate [path dx dy]
  (-> path
      svgpath
      (.translate dx dy)
      .toString))

(defn stitch [path]
  (s/replace path #"^M[, ]*[0-9.-]+[, ][, ]*[0-9.-]+" ""))
