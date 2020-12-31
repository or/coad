(ns or.coad.util
  (:require [clojure.string :as s]))

(defn upper-case-first [s]
  (str (s/upper-case (or (first s) "")) (s/join (rest s))))

(defn translate [keyword]
  (when keyword
    (-> keyword
        name
        (s/replace "-" " "))))

(defn translate-tincture [keyword]
  (case keyword
    :none "[no tincture]"
    (translate keyword)))

(defn translate-line [{:keys [style]}]
  (when (not= style :straight)
    (translate style)))

(defn translate-cap-first [keyword]
  (-> keyword
      translate
      upper-case-first))

(defn combine [separator words]
  (s/join separator (filter #(> (count %) 0) words)))

(defn contains-in?
  [m ks]
  (not= ::absent (get-in m ks ::absent)))
