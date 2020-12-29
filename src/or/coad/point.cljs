(ns or.coad.point
  (:require [or.coad.vector :as v]))

(def options
  [["Fess" :fess]
   ["Chief" :chief]
   ["Base" :base]
   ["Dexter" :dexter]
   ["Sinister" :sinister]
   ["Honour" :honour]
   ["Nombril" :nombril]])

(defn calculate [{:keys [point offset-x offset-y] :or {offset-x 0
                                                       offset-y 0}} environment default]
  (let [ref    (-> point
                   (or default))
        p      (-> environment :points (get ref))
        width  (:width environment)
        height (:height environment)
        dx     (-> offset-x
                   (* width)
                   (/ 100))
        dy     (-> offset-y
                   (* height)
                   (/ 100)
                   -)]
    (v/v (-> p
             :x
             (+ dx))
         (-> p
             :y
             (+ dy)))))