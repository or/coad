(ns or.coad.escutcheon
  (:require [or.coad.field-environment :as field-environment]))

(def heater
  ;; sqrt(3) / 2 * 6 ~ 5.196152422706632
  (field-environment/create
   (str "m 0,0"
        "h 3"
        "v 2"
        "a 6 6 0 0 1 -3,5.196152422706632"
        "a 6 6 0 0 1 -3,-5.196152422706632"
        "v -2"
        "z")
   {:context      :root
    :bounding-box [-3 3 0 (+ 2 5.196152422706632)]}))

(def square-french
  (field-environment/create
   (str "m 0,0"
        "v 15.7"
        "c 0,6 6,12 12,13"
        "c 6,-1 12,-7 12,-13"
        "V 0"
        "z")
   {:context      :root
    :bounding-box [0 (* 2 12) 0 (+ 15.7 13)]}))

(def square-iberian
  (field-environment/create
   (str "m 0,0"
        "h 5"
        "v 7"
        "a 5 5 0 0 1 -10,0"
        "v -7"
        "z")
   {:context      :root
    :bounding-box [-5 5 0 (+ 7 5)]}))

(def french-modern
  (field-environment/create
   (str "m 0,0"
        "h 7"
        "v 15"
        "a 1 1 0 0 1 -1,1"
        "h -5"
        "a 1 1 0 0 0 -1,1"
        "a 1 1 0 0 0 -1,-1"
        "h -5"
        "a 1 1 0 0 1 -1,-1"
        "v -15"
        "h 7"
        "z")
   {:context      :root
    :bounding-box [-7 7 0 (* 2 8)]}))

(def lozenge
  (field-environment/create
   (str "m 0,0"
        "L 5,6.5"
        "L 0,13"
        "L -5,6.5"
        "z")
   {:context      :root
    :bounding-box [-5 5 0 13]
    :points       {:fess {:x 0 :y 6.5}}}))

(def oval
  (field-environment/create
   (str "m 0,0"
        "A 5 6.8 0 0 1 5,6.5"
        "A 5 6.8 0 0 1 0,13"
        "A 5 6.8 0 0 1 -5,6.5"
        "A 5 6.8 0 0 1 0,0"
        "z")
   {:context      :root
    :bounding-box [-5 5 0 13]
    :points       {:fess {:x 0 :y 6.5}}}))

(def swiss
  ;; sqrt(3) / 2 * 6 ~ 5.196152422706632
  (field-environment/create
   (str "m 0,0"
        "a 6 6 0 0 0 3,0"
        "v 2"
        "a 6 6 0 0 1 -3,5.196152422706632"
        "a 6 6 0 0 1 -3,-5.196152422706632"
        "v -2"
        "a 6 6 0 0 0 3,0"
        "z")
   {:context      :root
    :bounding-box [-3 3 0 (+ 2 5.196152422706632)]}))

(def english
  (field-environment/create
   (str "m 0,0"
        "h 8"
        "a 1 1 0 0 0 -1,1"
        "v 14"
        "a 1 1 0 0 1 -1,1"
        "h -5"
        "a 1 1 0 0 0 -1,1"
        "a 1 1 0 0 0 -1,-1"
        "h -5"
        "a 1 1 0 0 1 -1,-1"
        "v -14"
        "a 1 1 0 0 0 -1,-1"
        "h 8"
        "z")
   {:context      :root
    :bounding-box [-8 8 0 (* 2 8)]}))

(def old-german
  (field-environment/create
   (str "M 72.11366,0.02295528"
        "C 57.750955,0.37237769 45.027606,4.7621231 34.431166,15.271832 23.071059,8.4719474 6.0192233,14.846714 3.7516183,27.606377 "
        "c 1.5406391,-1.040157 3.3887379,-1.60518 5.3769154,-1.60518 5.3575813,0 9.7120813,4.370822 9.7120813,9.800052 0,5.429223 -4.353826,9.841954 -9.7120813,9.841954 -1.6206957,0 -3.1579959,-0.385244 -4.5019019,-1.098283 "
        "l 0.08334,0.802926 "
        "C 4.7099718,59.689039 5.0666945e-7,65.940793 5.0666945e-7,78.761283 5.0666945e-7,105.19372 25.197238,130 50.604594,130 "
        "c 25.407328,0 49.395405,-23.22884 49.395405,-50.35198 0,-20.785566 -16.173452,-30.737686 -16.173452,-50.68991 0,-19.9522286 8.837041,-27.1191057 8.837041,-27.1191057 "
        "C 85.496569,0.53593494 78.642378,-0.13587312 72.113629,0.02295528 "
        "Z")
   {:context      :root
    :bounding-box [0 100 0 130]
    :points       {:fess {:x 50 :y 65}}}))

(def polish
  (field-environment/create
   (str "m 43.402145,5e-7 "
        "c -8.662508,0 -14.063932,7.322064 -27.53457,9.380727 0.01086,7.9371285 -3.321499,15.7448405 -7.7644202,20.8881635 0,0 8.6550412,4.035941 8.6550412,12.967045 0,13.48538 -14.3402146,13.50873 -14.3402146,13.50873 0,0 -2.4179809,4.962539 -2.4179809,15.009696 0,22.996861 15.7236635,40.377428 27.6621895,45.737558 11.938525,5.36013 18.80961,7.63894 22.359194,12.50808 3.549585,-4.86914 10.377904,-7.14795 22.316426,-12.50808 11.938526,-5.36013 27.662185,-22.742701 27.662185,-45.737557 0,-10.047158 -2.41798,-15.009697 -2.41798,-15.009697 0,0 -14.340209,-0.02335 -14.340209,-13.50873 0,-8.931104 8.655042,-12.967045 8.655042,-12.967045 "
        "C 87.453242,25.123567 84.122242,17.317856 84.132428,9.3807275 70.661111,7.3213975 65.259687,5.0000001e-7 56.597858,5.0000001e-7 51.658715,5.0000001e-7 50.021384,2.5016165 50.021384,2.5016165 "
        "c 0,0 -1.680096,-2.50161599999999 -6.619239,-2.501616 "
        "z")
   {:context      :root
    :bounding-box [0 100 0 130]
    :points       {:fess {:x 50 :y 60}}}))

(def polish-19th-century
  (field-environment/create
   (str
    "M 9.5919374,7.6420451e-7 6.7196191e-7,9.9320533 "
    "C 13.91585,26.565128 6.4383768,51.856026 6.0545095,76.190405 5.7210271,97.330758 24.557556,120 50.136084,120 75.714614,120 94.551144,97.330758 94.217662,76.190405 93.833795,51.856026 86.356321,26.565129 100.27217,9.9320533 "
    "L 90.680234,7.6420451e-7 "
    "C 81.317854,12.169833 65.149597,3.8094085 50.136084,3.8094085 35.122571,3.8094085 18.954318,12.169833 9.5919374,7.6420451e-7 "
    "Z")
   {:context      :root
    :bounding-box [0 100 0 120]
    :points       {:fess {:x 50 :y 60}}}))

(def renaissance
  (field-environment/create
   (str
    "M 43.672061,112.35743 "
    "C 20.076921,107.21428 1.2267205,96.616647 5.1084778e-7,62.761658 9.9757105,57.299078 13.336031,28.673358 3.0804505,13.816518 "
    "L 9.0622405,3.6100493 "
    "C 28.967341,6.8985193 35.708501,-4.5443607 50,2.1304593 "
    "c 14.2915,-6.67482 21.03266,4.76806 40.93775,1.47959 "
    "l 5.9818,10.2064687 "
    "C 86.66397,28.673358 90.02428,57.299078 100,62.761658 98.77327,96.616647 79.92307,107.21428 56.32792,112.35743 51.60688,113.38653 51.68278,114.71878 50,117 "
    "c -1.68279,-2.28122 -1.60689,-3.61347 -6.327939,-4.64257 "
    "Z")
   {:context      :root
    :bounding-box [0 100 0 117]
    :points       {:fess {:x 50 :y 55}}}))

(def kinds
  [["Heater" :heater heater]
   ["Square French" :square-french square-french]
   ["Square Iberian" :square-iberian square-iberian]
   ["French Modern" :french-modern french-modern]
   ["Lozenge" :lozenge lozenge]
   ["Oval" :oval oval]
   ["Renaissance" :renaissance renaissance]
   ["Swiss" :swiss swiss]
   ["English" :english english]
   ["Ugly shape for testing" :old-german old-german]
   ["Polish" :polish polish]
   ["Polish (19th century)" :polish-19th-century polish-19th-century]])

(def kinds-map
  (->> kinds
       (map (fn [[_ key data]]
              [key data]))
       (into {})))

(def options
  (->> kinds
       (map (fn [[name key _]]
              [key name]))))

(defn field [type]
  (get kinds-map type))
