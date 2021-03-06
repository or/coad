(ns or.coad.geometry)

(def default-options
  {:size      {:type    :range
               :min     5
               :max     100
               :default 50}
   :stretch   {:type    :range
               :min     0.33
               :max     3
               :default 1}
   :mirrored? {:type    :boolean
               :default false}
   :reversed? {:type    :boolean
               :default false}
   :rotation  {:type    :range
               :min     -180
               :max     180
               :default 0}})
