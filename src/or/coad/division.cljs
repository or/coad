(ns or.coad.division
  (:require [or.coad.tinctures :refer [tinctures]]))

(defn base-area [fill]
  [:rect {:x      -1000
          :y      -1000
          :width  2000
          :height 2000
          :fill   fill}])

;; TODO: masks for each field with a defined center and a scale
(defn per-pale [[left right]]
  [:<>
   [base-area (get tinctures left)]
   [:path {:d    "m 0,-1000 h 2000 v 2000 h -2000 z"
           :fill (get tinctures right)}]])

(defn per-fess [[top bottom]]
  [:<>
   [base-area (get tinctures top)]
   [:path {:d    "m -1000,0 h 2000 v 2000 h -2000 z"
           :fill (get tinctures bottom)}]])

(defn per-bend [[top bottom]]
  [:<>
   [base-area (get tinctures top)]
   [:path {:d    "m -1000,-1000 v 2000 h 2000 z"
           :fill (get tinctures bottom)}]])

(defn per-bend-sinister [[top bottom]]
  [:<>
   [base-area (get tinctures top)]
   [:path {:d    "m 1000,-1000 v 2000 h -2000 z"
           :fill (get tinctures bottom)}]])

(defn per-chevron [[top bottom]]
  [:<>
   [base-area (get tinctures top)]
   [:path {:d    "m 0,0 l 1000,1000 h -2000 z"
           :fill (get tinctures bottom)}]])

(defn per-saltire [[vertical horizontal]]
  [:<>
   [base-area (get tinctures vertical)]
   [:path {:d    "m 0,0 l -1000,-1000 v 2000 z"
           :fill (get tinctures horizontal)}]
   [:path {:d    "m 0,0 l 1000,-1000 v 2000 z"
           :fill (get tinctures horizontal)}]])

(defn quarterly [[left right]]
  [:<>
   [base-area (get tinctures left)]
   [:path {:d    "m 0,0 h 1000 v -1000 h -1000 z"
           :fill (get tinctures right)}]
   [:path {:d    "m 0,0 h -1000 v 1000 h 1000 z"
           :fill (get tinctures right)}]])

(defn gyronny [[left right]]
  [:<>
   [base-area (get tinctures left)]
   [:path {:d    "m 0,0 v -1000 h 1000 z"
           :fill (get tinctures right)}]
   [:path {:d    "m 0,0 h 1000 v 1000 z"
           :fill (get tinctures right)}]
   [:path {:d    "m 0,0 v 1000 h -1000 z"
           :fill (get tinctures right)}]
   [:path {:d    "m 0,0 h -1000 v -1000 z"
           :fill (get tinctures right)}]])

(defn tierced-in-pale [[left middle right]]
  [:<>
   [base-area (get tinctures left)]
   [:path {:d    "m -16.666666,-1000 h 2000 v 2000 h -2000 z"
           :fill (get tinctures middle)}]
   [:path {:d    "m 16.666666,-1000 h 2000 v 2000 h -2000 z"
           :fill (get tinctures right)}]])

(defn tierced-in-fesse [[top middle bottom]]
  [:<>
   [base-area (get tinctures top)]
   [:path {:d    "m -1000,-16.666666 h 2000 v 2000 h -2000 z"
           :fill (get tinctures middle)}]
   [:path {:d    "m -1000,16.666666 h 2000 v 2000 h -2000 z"
           :fill (get tinctures bottom)}]])

(defn tierced-in-pairle [[left right bottom]]
  [:<>
   [base-area (get tinctures left)]
   [:path {:d    "m 0,-1000 h 2000 v 2000 h -2000 z"
           :fill (get tinctures right)}]
   [:path {:d    "m 0,0 l 1000,1000 h -2000 z"
           :fill (get tinctures bottom)}]])

;; TODO: offset and/or number of stripes to fit, which dictates their width
(defn paly [[base stripe]]
  [:<>
   [base-area (get tinctures base)]
   [:path {:d    "m -37.5,-1000 h 12.5 v 2000 h -12.5 z"
           :fill (get tinctures stripe)}]
   [:path {:d    "m -12.5,-1000 h 12.5 v 2000 h -12.5 z"
           :fill (get tinctures stripe)}]
   [:path {:d    "m 12.5,-1000 h 12.5 v 2000 h -12.5 z"
           :fill (get tinctures stripe)}]
   [:path {:d    "m 37.5,-1000 h 12.5 v 2000 h -12.5 z"
           :fill (get tinctures stripe)}]])

(defn barry [[base stripe]]
  [:<>
   [base-area (get tinctures base)]
   [:path {:d    "m -1000,-37.5 v 12.5 h 2000 v -12.5 z"
           :fill (get tinctures stripe)}]
   [:path {:d    "m -1000,-12.5 v 12.5 h 2000 v -12.5 z"
           :fill (get tinctures stripe)}]
   [:path {:d    "m -1000,12.5 v 12.5 h 2000 v -12.5 z"
           :fill (get tinctures stripe)}]
   [:path {:d    "m -1000,37.5 v 12.5 h 2000 v -12.5 z"
           :fill (get tinctures stripe)}]
   [:path {:d    "m -1000,62.5 v 12.5 h 2000 v -12.5 z"
           :fill (get tinctures stripe)}]])

(defn bendy [[base stripe]]
  [:<>
   [base-area (get tinctures base)]
   [:path {:d    "m -1000,-1000 v 25 l 2000,2000 v -25 z"
           :fill (get tinctures stripe)}]
   [:path {:d    "m -1000,-1050 v 25 l 2000,2000 v -25 z"
           :fill (get tinctures stripe)}]
   [:path {:d    "m -1000,-1100 v 25 l 2000,2000 v -25 z"
           :fill (get tinctures stripe)}]
   [:path {:d    "m -1000,-950 v 25 l 2000,2000 v -25 z"
           :fill (get tinctures stripe)}]
   [:path {:d    "m -1000,-900 v 25 l 2000,2000 v -25 z"
           :fill (get tinctures stripe)}]])

(defn bendy-sinister [[base stripe]]
  [:<>
   [base-area (get tinctures base)]
   [:path {:d    "m 1000,-1025 v 25 l -2000,2000 v -25 z"
           :fill (get tinctures stripe)}]
   [:path {:d    "m 1000,-1075 v 25 l -2000,2000 v -25 z"
           :fill (get tinctures stripe)}]
   [:path {:d    "m 1000,-1125 v 25 l -2000,2000 v -25 z"
           :fill (get tinctures stripe)}]
   [:path {:d    "m 1000,-975 v 25 l -2000,2000 v -25 z"
           :fill (get tinctures stripe)}]
   [:path {:d    "m 1000,-925 v 25 l -2000,2000 v -25 z"
           :fill (get tinctures stripe)}]])

(def kinds
  [["Per Pale" :per-pale per-pale]
   ["Per Fess" :per-fess per-fess]
   ["Per Bend" :per-bend per-bend]
   ["Per Bend Sinister" :per-bend-sinister per-bend-sinister]
   ["Per Chevron" :per-chevron per-chevron]
   ["Per Saltire" :per-saltire per-saltire]
   ["Quarterly" :quarterly quarterly]
   ["Gyronny" :gyronny gyronny]
   ["Tierced in Pale" :tierced-in-pale tierced-in-pale]
   ["Tierced in Fesse" :tierced-in-fesse tierced-in-fesse]
   ["Tierced in Pairle" :tierced-in-pairle tierced-in-pairle]
   ["Paly" :paly paly]
   ["Barry" :barry barry]
   ["Bendy" :bendy bendy]
   ["Bendy Sinister" :bendy-sinister bendy-sinister]])

(def kinds-function-map
  (->> kinds
       (map (fn [[_ key function]]
              [key function]))
       (into {})))

(def options
  (->> kinds
       (map (fn [[name key _]]
              [key name]))))

(defn render [{:keys [type parts]}]
  (let [function (get kinds-function-map type)]
    [function parts]))