(ns or.coad.division
  (:require [or.coad.field-environment :as field-environment]
            [or.coad.line :as line]
            [or.coad.svg :as svg]
            [or.coad.vector :as v]))

(defn get-field [fields index]
  (let [part (get fields index)]
    (if (number? part)
      (get fields part)
      part)))

(defn per-pale [{:keys [fields line] :as field} environment top-level-render options]
  (let [mask-id-1    (svg/id "division-pale-1")
        mask-id-2    (svg/id "division-pale-2")
        top-left     (get-in environment [:points :top-left])
        top-right    (get-in environment [:points :top-right])
        bottom-left  (get-in environment [:points :bottom-left])
        bottom-right (get-in environment [:points :bottom-right])
        chief        (get-in environment [:points :chief])
        base         (get-in environment [:points :base])
        line-style   (or (:style line) :straight)
        {line :line} (line/create line-style
                                  (:y (v/- base chief))
                                  :angle -90)
        field-1      (field-environment/create
                      (svg/make-path ["M" base
                                      (line/stitch line)
                                      "L" chief
                                      "L" top-left
                                      "L" bottom-left
                                      "z"])
                      {:parent  field
                       :context [:per-pale :left]})
        field-2      (field-environment/create
                      (svg/make-path ["M" base
                                      (line/stitch line)
                                      "L" chief
                                      "L" top-right
                                      "L" bottom-right
                                      "z"])
                      {:parent  field
                       :context [:per-pale :left]})]
    [:<>
     [:defs
      [:mask {:id mask-id-1}
       [:path {:d    (:shape field-1)
               :fill "#fff"}]
       [:path.overlap {:d (:shape field-1)}]]
      [:mask {:id mask-id-2}
       [:path {:d    (:shape field-2)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" chief
                            "L" top-right
                            "L" bottom-right
                            "L" base])}]]]
     [:g {:mask (str "url(#" mask-id-1 ")")}
      [top-level-render (get fields 0) field-1 options]]
     [:g {:mask (str "url(#" mask-id-2 ")")}
      [top-level-render (get fields 1) field-2 options]]
     (when (:outline? options)
       [:g.outline
        [:path {:d (svg/make-path
                    ["M" chief
                     "L" base])}]])]))

(defn per-fess [{:keys [fields line] :as field} environment top-level-render options]
  (let [mask-id-1    (svg/id "division-fess-1")
        mask-id-2    (svg/id "division-fess-2")
        top-left     (get-in environment [:points :top-left])
        top-right    (get-in environment [:points :top-right])
        bottom-left  (get-in environment [:points :bottom-left])
        bottom-right (get-in environment [:points :bottom-right])
        dexter       (get-in environment [:points :dexter])
        sinister     (get-in environment [:points :sinister])
        line-style   (or (:style line) :straight)
        {line :line} (line/create line-style
                                  (:x (v/- sinister dexter)))
        field-1      (field-environment/create
                      (svg/make-path ["M" dexter
                                      (line/stitch line)
                                      "L" sinister
                                      "L" top-right
                                      "L" top-left
                                      "z"])
                      {:parent  field
                       :context [:per-fess :top]})
        field-2      (field-environment/create
                      (svg/make-path ["M" dexter
                                      (line/stitch line)
                                      "L" sinister
                                      "L" bottom-right
                                      "L" bottom-left
                                      "z"])
                      {:parent  field
                       :context [:per-fess :bottom]})]
    [:<>
     [:defs
      [:mask {:id mask-id-1}
       [:path {:d    (:shape field-1)
               :fill "#fff"}]
       [:path.overlap {:d (:shape field-1)}]]
      [:mask {:id mask-id-2}
       [:path {:d    (:shape field-2)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" sinister
                            "L" bottom-right
                            "L" bottom-left
                            "L" dexter])}]]]
     [:g {:mask (str "url(#" mask-id-1 ")")}
      [top-level-render (get fields 0) field-1 options]]
     [:g {:mask (str "url(#" mask-id-2 ")")}
      [top-level-render (get fields 1) field-2 options]]
     (when (:outline? options)
       [:g.outline
        [:path {:d (svg/make-path
                    ["M" sinister
                     "L" dexter])}]])]))

(defn per-bend [{:keys [fields line] :as field} environment top-level-render options]
  (let [mask-id-1         (svg/id "division-bend-1")
        mask-id-2         (svg/id "division-bend-2")
        top-left          (get-in environment [:points :top-left])
        top-right         (get-in environment [:points :top-right])
        bottom-left       (get-in environment [:points :bottom-left])
        bottom-right      (get-in environment [:points :bottom-right])
        fess              (get-in environment [:points :fess])
        bend-intersection (v/project top-left fess (:x top-right))
        line-style        (or (:style line) :straight)
        {line :line}      (line/create line-style
                                       (v/abs (v/- bend-intersection top-left))
                                       :angle 45)
        field-1           (field-environment/create
                           (svg/make-path ["M" top-left
                                           (line/stitch line)
                                           "L" bend-intersection
                                           "L" top-right
                                           "z"])
                           {:parent  field
                            :context [:per-bend :top]})
        field-2           (field-environment/create
                           (svg/make-path ["M" top-left
                                           (line/stitch line)
                                           "L" bend-intersection
                                           "L" bottom-right
                                           "L" bottom-left
                                           "z"])
                           {:parent  field
                            :context [:per-bend :bottom]})]
    [:<>
     [:defs
      [:mask {:id mask-id-1}
       [:path {:d    (:shape field-1)
               :fill "#fff"}]
       [:path.overlap {:d (:shape field-1)}]]
      [:mask {:id mask-id-2}
       [:path {:d    (:shape field-2)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" bend-intersection
                            "L" bottom-right
                            "L" bottom-left
                            "L" top-left])}]]]
     [:g {:mask (str "url(#" mask-id-1 ")")}
      [top-level-render (get fields 0) field-1 options]]
     [:g {:mask (str "url(#" mask-id-2 ")")}
      [top-level-render (get fields 1) field-2 options]]
     (when (:outline? options)
       [:g.outline
        [:path {:d (svg/make-path
                    ["M" bend-intersection
                     "L" top-left])}]])]))

(defn per-bend-sinister [{:keys [fields line] :as field} environment top-level-render options]
  (let [mask-id-1                  (svg/id "division-bend-sinister-1")
        mask-id-2                  (svg/id "division-bend-sinister-2")
        top-left                   (get-in environment [:points :top-left])
        top-right                  (get-in environment [:points :top-right])
        bottom-left                (get-in environment [:points :bottom-left])
        bottom-right               (get-in environment [:points :bottom-right])
        fess                       (get-in environment [:points :fess])
        bend-intersection          (v/project top-right fess (:x top-left))
        line-style                 (or (:style line) :straight)
        {line        :line
         line-length :length}      (line/create line-style
                                                (v/abs (v/- bend-intersection top-right))
                                                :angle -45
                                                :flipped? false)
        bend-intersection-adjusted (v/extend
                                       top-right
                                     bend-intersection
                                     line-length)
        field-1                    (field-environment/create
                                    (svg/make-path ["M" bend-intersection-adjusted
                                                    (line/stitch line)
                                                    "L" top-right
                                                    "L" top-left
                                                    "z"])
                                    {:parent  field
                                     :context [:per-bend-sinister :top]})
        field-2                    (field-environment/create
                                    (svg/make-path ["M" bend-intersection-adjusted
                                                    (line/stitch line)
                                                    "L" top-right
                                                    "L" bottom-right
                                                    "L" bottom-left
                                                    "z"])
                                    {:parent  field
                                     :context [:per-bend-sinister :bottom]})]
    [:<>
     [:defs
      [:mask {:id mask-id-1}
       [:path {:d    (:shape field-1)
               :fill "#fff"}]
       [:path.overlap {:d (:shape field-1)}]]
      [:mask {:id mask-id-2}
       [:path {:d    (:shape field-2)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" top-right
                            "L" bottom-right
                            "L" bottom-left
                            "L" bend-intersection-adjusted])}]]]
     [:g {:mask (str "url(#" mask-id-1 ")")}
      [top-level-render (get fields 0) field-1 options]]
     [:g {:mask (str "url(#" mask-id-2 ")")}
      [top-level-render (get fields 1) field-2 options]]
     (when (:outline? options)
       [:g.outline
        [:path {:d (svg/make-path
                    ["M" bend-intersection-adjusted
                     "L" top-right])}]])]))

(defn per-chevron [{:keys [fields line] :as field} environment top-level-render options]
  (let [mask-id-1                         (svg/id "division-chevron-1")
        mask-id-2                         (svg/id "division-chevron-2")
        line-style                        (or (:style line) :straight)
        top-left                          (get-in environment [:points :top-left])
        top-right                         (get-in environment [:points :top-right])
        bottom-left                       (get-in environment [:points :bottom-left])
        bottom-right                      (get-in environment [:points :bottom-right])
        fess                              (get-in environment [:points :fess])
        bend-intersection-dexter          (v/project top-right fess (:x top-left))
        bend-intersection-sinister        (v/project top-left fess (:x top-right))
        {line-dexter        :line
         line-dexter-length :length}      (line/create line-style
                                                       (v/abs (v/- bend-intersection-dexter fess))
                                                       :angle -45)
        {line-sinister :line}             (line/create line-style
                                                       (v/abs (v/- bend-intersection-sinister fess))
                                                       :angle 45)
        bend-intersection-dexter-adjusted (v/extend fess bend-intersection-dexter line-dexter-length)
        field-1                           (field-environment/create
                                           (svg/make-path ["M" fess
                                                           (line/stitch line-sinister)
                                                           "L" bend-intersection-sinister
                                                           "L" top-right
                                                           "L" top-left
                                                           "L" bend-intersection-dexter-adjusted
                                                           (line/stitch line-dexter)
                                                           "z"])
                                           {:parent  field
                                            :context [:per-chevron :top]})
        field-2                           (field-environment/create
                                           (svg/make-path ["M" fess
                                                           (line/stitch line-sinister)
                                                           "L" bend-intersection-sinister
                                                           "L" bottom-right
                                                           "L" bottom-left
                                                           "L" bend-intersection-dexter-adjusted
                                                           (line/stitch line-dexter)
                                                           "z"])
                                           {:parent  field
                                            :context [:per-chevron :bottom]})]
    [:<>
     [:defs
      [:mask {:id mask-id-1}
       [:path {:d    (:shape field-1)
               :fill "#fff"}]
       [:path.overlap {:d (:shape field-1)}]]
      [:mask {:id mask-id-2}
       [:path {:d    (:shape field-2)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" bend-intersection-sinister
                            "L" bottom-right
                            "L" bottom-left
                            "L" bend-intersection-dexter-adjusted])}]]]
     [:g {:mask (str "url(#" mask-id-1 ")")}
      [top-level-render (get fields 0) field-1 options]]
     [:g {:mask (str "url(#" mask-id-2 ")")}
      [top-level-render (get fields 1) field-2 options]]
     (when (:outline? options)
       [:g.outline
        [:path {:d (svg/make-path
                    ["M" bend-intersection-dexter-adjusted
                     (line/stitch line-dexter)
                     "L" fess
                     (line/stitch line-sinister)])}]])]))

(defn per-saltire [{:keys [fields line] :as field} environment top-level-render options]
  (let [mask-id-1                           (svg/id "division-saltire-1")
        mask-id-2                           (svg/id "division-saltire-2")
        mask-id-3                           (svg/id "division-saltire-3")
        mask-id-4                           (svg/id "division-saltire-4")
        line-style                          (or (:style line) :straight)
        top-left                            (get-in environment [:points :top-left])
        top-right                           (get-in environment [:points :top-right])
        bottom-left                         (get-in environment [:points :bottom-left])
        bottom-right                        (get-in environment [:points :bottom-right])
        fess                                (get-in environment [:points :fess])
        bend-intersection-sinister          (v/project top-left fess (:x top-right))
        bend-intersection-dexter            (v/project top-right fess (:x top-left))
        {line-chief-dexter        :line
         line-chief-dexter-length :length}  (line/create line-style
                                                         (v/abs (v/- top-left fess))
                                                         :angle 45
                                                         :reversed? true)
        {line-chief-sinister :line}         (line/create line-style
                                                         (v/abs (v/- top-right fess))
                                                         :angle -45
                                                         :flipped? true)
        {line-base-sinister        :line
         line-base-sinister-length :length} (line/create line-style
                                                         (v/abs (v/- bend-intersection-sinister fess))
                                                         :angle 225
                                                         :reversed? true)
        {line-base-dexter :line}            (line/create line-style
                                                         (v/abs (v/- bend-intersection-dexter fess))
                                                         :angle -225
                                                         :flipped? true)
        top-left-adjusted                   (v/extend
                                                fess
                                              top-left
                                              line-chief-dexter-length)
        bend-intersection-sinister-adjusted (v/extend
                                                fess
                                              bend-intersection-sinister
                                              line-base-sinister-length)
        field-1                             (field-environment/create
                                             (svg/make-path ["M" top-left-adjusted
                                                             (line/stitch line-chief-dexter)
                                                             "L" fess
                                                             (line/stitch line-chief-sinister)
                                                             "L" top-right
                                                             "z"])
                                             {:parent  field
                                              :context [:per-saltire :top]})
        field-2                             (field-environment/create
                                             (svg/make-path ["M" fess
                                                             (line/stitch line-chief-sinister)
                                                             "L" top-right
                                                             "L" bend-intersection-sinister-adjusted
                                                             (line/stitch line-base-sinister)
                                                             "z"])
                                             {:parent  field
                                              :context [:per-saltire :right]})
        field-3                             (field-environment/create
                                             (svg/make-path ["M" bend-intersection-sinister-adjusted
                                                             (line/stitch line-base-sinister)
                                                             "L" fess
                                                             (line/stitch line-base-dexter)
                                                             "L" bend-intersection-dexter
                                                             "L" bottom-left
                                                             "L" bottom-right
                                                             "z"])
                                             {:parent  field
                                              :context [:per-saltire :bottom]})
        field-4                             (field-environment/create
                                             (svg/make-path ["M" fess
                                                             (line/stitch line-base-dexter)
                                                             "L" bend-intersection-dexter
                                                             "L" top-left-adjusted
                                                             (line/stitch line-chief-dexter)
                                                             "z"])
                                             {:parent  field
                                              :context [:per-saltire :left]})]
    [:<>
     [:defs
      [:mask {:id mask-id-1}
       [:path {:d    (:shape field-1)
               :fill "#fff"}]
       [:path.overlap {:d (:shape field-1)}]]
      [:mask {:id mask-id-2}
       [:path {:d    (:shape field-2)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" top-right
                            "L" bend-intersection-sinister-adjusted
                            (line/stitch line-base-sinister)
                            "L" fess])}]]
      [:mask {:id mask-id-3}
       [:path {:d    (:shape field-3)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" fess
                            (line/stitch line-base-dexter)
                            "L" bend-intersection-dexter
                            "L" bottom-left
                            "L" bottom-right
                            "L" bend-intersection-sinister-adjusted])}]]
      [:mask {:id mask-id-4}
       [:path {:d    (:shape field-4)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" bend-intersection-dexter
                            "L" top-left-adjusted])}]]]
     [:g {:mask (str "url(#" mask-id-1 ")")}
      [top-level-render (get-field fields 0) field-1 options]]
     [:g {:mask (str "url(#" mask-id-2 ")")}
      [top-level-render (get-field fields 1) field-2 options]]
     [:g {:mask (str "url(#" mask-id-3 ")")}
      [top-level-render (get-field fields 2) field-3 options]]
     [:g {:mask (str "url(#" mask-id-4 ")")}
      [top-level-render (get-field fields 3) field-4 options]]
     (when (:outline? options)
       [:g.outline
        [:path {:d (svg/make-path
                    ["M" top-left-adjusted
                     (line/stitch line-chief-dexter)])}]
        [:path {:d (svg/make-path
                    ["M" fess
                     (line/stitch line-chief-sinister)])}]
        [:path {:d (svg/make-path
                    ["M" bend-intersection-sinister-adjusted
                     (line/stitch line-base-sinister)])}]
        [:path {:d (svg/make-path
                    ["M" fess
                     (line/stitch line-base-dexter)])}]])]))

(defn quarterly [{:keys [fields line] :as field} environment top-level-render options]
  (let [mask-id-1                   (svg/id "division-quarterly-1")
        mask-id-2                   (svg/id "division-quarterly-2")
        mask-id-3                   (svg/id "division-quarterly-3")
        mask-id-4                   (svg/id "division-quarterly-4")
        line-style                  (or (:style line) :straight)
        top-left                    (get-in environment [:points :top-left])
        top-right                   (get-in environment [:points :top-right])
        bottom-left                 (get-in environment [:points :bottom-left])
        bottom-right                (get-in environment [:points :bottom-right])
        chief                       (get-in environment [:points :chief])
        base                        (get-in environment [:points :base])
        fess                        (get-in environment [:points :fess])
        dexter                      (get-in environment [:points :dexter])
        sinister                    (get-in environment [:points :sinister])
        {line-chief        :line
         line-chief-length :length} (line/create line-style
                                                 (v/abs (v/- chief fess))
                                                 :angle 90
                                                 :reversed? true)
        {line-sinister :line}       (line/create line-style
                                                 (v/abs (v/- sinister fess))
                                                 :flipped? true)
        {line-base        :line
         line-base-length :length}  (line/create line-style
                                                 (v/abs (v/- base fess))
                                                 :angle -90
                                                 :reversed? true)
        {line-dexter :line}         (line/create line-style
                                                 (v/abs (v/- dexter fess))
                                                 :angle -180
                                                 :flipped? true)
        chief-adjusted              (v/extend fess chief line-chief-length)
        base-adjusted               (v/extend fess base line-base-length)
        field-1                     (field-environment/create
                                     (svg/make-path ["M" chief-adjusted
                                                     (line/stitch line-chief)
                                                     "L" fess
                                                     (line/stitch line-dexter)
                                                     "L" dexter
                                                     "L" top-left
                                                     "z"])
                                     {:parent  field
                                      :context [:per-quarterly :top-left]})
        field-2                     (field-environment/create
                                     (svg/make-path ["M" chief-adjusted
                                                     (line/stitch line-chief)
                                                     "L" fess
                                                     (line/stitch line-sinister)
                                                     "L" sinister
                                                     "L" top-right
                                                     "z"])
                                     {:parent  field
                                      :context [:per-quarterly :top-right]})
        field-3                     (field-environment/create
                                     (svg/make-path ["M" fess
                                                     (line/stitch line-sinister)
                                                     "L" sinister
                                                     "L" bottom-right
                                                     "L" base-adjusted
                                                     (line/stitch line-base)
                                                     "z"])
                                     {:parent  field
                                      :context [:per-quarterly :bottom-right]})
        field-4                     (field-environment/create
                                     (svg/make-path ["M" base-adjusted
                                                     (line/stitch line-base)
                                                     "L" fess
                                                     (line/stitch line-dexter)
                                                     "L" dexter
                                                     "L" bottom-left
                                                     "z"])
                                     {:parent  field
                                      :context [:per-quarterly :bottom-left]})]
    [:<>
     [:defs
      [:mask {:id mask-id-1}
       [:path {:d    (:shape field-1)
               :fill "#fff"}]
       [:path.overlap {:d (:shape field-1)}]]
      [:mask {:id mask-id-2}
       [:path {:d    (:shape field-2)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" fess
                            (line/stitch line-sinister)
                            "L" sinister
                            "L" top-right
                            "L" chief-adjusted])}]]
      [:mask {:id mask-id-3}
       [:path {:d    (:shape field-3)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" sinister
                            "L" bottom-right
                            "L" base-adjusted
                            (line/stitch line-base)
                            "L" fess])}]]
      [:mask {:id mask-id-4}
       [:path {:d    (:shape field-4)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" dexter
                            "L" bottom-left
                            "L" base-adjusted])}]]]
     [:g {:mask (str "url(#" mask-id-1 ")")}
      [top-level-render (get-field fields 0) field-1 options]]
     [:g {:mask (str "url(#" mask-id-2 ")")}
      [top-level-render (get-field fields 1) field-2 options]]
     [:g {:mask (str "url(#" mask-id-3 ")")}
      [top-level-render (get-field fields 2) field-3 options]]
     [:g {:mask (str "url(#" mask-id-4 ")")}
      [top-level-render (get-field fields 3) field-4 options]]
     (when (:outline? options)
       [:g.outline
        [:path {:d (svg/make-path
                    ["M" chief-adjusted
                     (line/stitch line-chief)])}]
        [:path {:d (svg/make-path
                    ["M" fess
                     (line/stitch line-sinister)])}]
        [:path {:d (svg/make-path
                    ["M" base-adjusted
                     (line/stitch line-base)])}]
        [:path {:d (svg/make-path
                    ["M" fess
                     (line/stitch line-dexter)])}]])]))

(defn gyronny [{:keys [fields line] :as field} environment top-level-render options]
  (let [mask-id-1                      (svg/id "division-gyronny-1")
        mask-id-2                      (svg/id "division-gyronny-2")
        mask-id-3                      (svg/id "division-gyronny-3")
        mask-id-4                      (svg/id "division-gyronny-4")
        mask-id-5                      (svg/id "division-gyronny-5")
        mask-id-6                      (svg/id "division-gyronny-6")
        mask-id-7                      (svg/id "division-gyronny-7")
        mask-id-8                      (svg/id "division-gyronny-8")
        line-style                     (or (:style line) :straight)
        top-left                       (get-in environment [:points :top-left])
        top-right                      (get-in environment [:points :top-right])
        bottom-left                    (get-in environment [:points :bottom-left])
        bottom-right                   (get-in environment [:points :bottom-right])
        chief                          (get-in environment [:points :chief])
        base                           (get-in environment [:points :base])
        fess                           (get-in environment [:points :fess])
        dexter                         (get-in environment [:points :dexter])
        sinister                       (get-in environment [:points :sinister])
        {line-chief        :line
         line-chief-length :length}    (line/create line-style
                                                    (v/abs (v/- chief fess))
                                                    :angle 90
                                                    :reversed? true)
        {line-sinister        :line
         line-sinister-length :length} (line/create line-style
                                                    (v/abs (v/- sinister fess))
                                                    :reversed? true
                                                    :angle 180)
        {line-base        :line
         line-base-length :length}     (line/create line-style
                                                    (v/abs (v/- base fess))
                                                    :angle -90
                                                    :reversed? true)
        {line-dexter        :line
         line-dexter-length :length}   (line/create line-style
                                                    (v/abs (v/- dexter fess))
                                                    :reversed? true)
        chief-adjusted                 (v/extend fess chief line-chief-length)
        base-adjusted                  (v/extend fess base line-base-length)
        dexter-adjusted                (v/extend fess dexter line-dexter-length)
        sinister-adjusted              (v/extend fess sinister line-sinister-length)
        bend-intersection-dexter       (v/project top-right fess (:x top-left))
        bend-intersection-sinister     (v/project top-left fess (:x top-right))
        {line-chief-dexter :line}      (line/create line-style
                                                    (v/abs (v/- top-left fess))
                                                    :flipped? true
                                                    :angle -135)
        {line-chief-sinister :line}    (line/create line-style
                                                    (v/abs (v/- top-right fess))
                                                    :flipped? true
                                                    :angle -45)
        {line-base-sinister :line}     (line/create line-style
                                                    (v/abs (v/- bend-intersection-sinister fess))
                                                    :flipped? true
                                                    :angle 45)
        {line-base-dexter :line}       (line/create line-style
                                                    (v/abs (v/- bend-intersection-dexter fess))
                                                    :flipped? true
                                                    :angle -225)
        field-1                        (field-environment/create
                                        (svg/make-path ["M" fess
                                                        (line/stitch line-chief-dexter)
                                                        "L" top-left
                                                        "L" chief-adjusted
                                                        (line/stitch line-chief)
                                                        "z"])
                                        {:parent  field
                                         :context [:per-gyronny :one]})
        field-2                        (field-environment/create
                                        (svg/make-path ["M" chief-adjusted
                                                        (line/stitch line-chief)
                                                        "L" fess
                                                        (line/stitch line-chief-sinister)
                                                        "L" top-right
                                                        "z"])
                                        {:parent  field
                                         :context [:per-gyronny :two]})
        field-3                        (field-environment/create
                                        (svg/make-path ["M" fess
                                                        (line/stitch line-chief-sinister)
                                                        "L" top-right
                                                        "L" sinister-adjusted
                                                        (line/stitch line-sinister)
                                                        "z"])
                                        {:parent  field
                                         :context [:per-gyronny :three]})
        field-4                        (field-environment/create
                                        (svg/make-path ["M" sinister-adjusted
                                                        (line/stitch line-sinister)
                                                        "L" fess
                                                        (line/stitch line-base-sinister)
                                                        "L" bend-intersection-sinister
                                                        "z"])
                                        {:parent  field
                                         :context [:per-gyronny :four]})
        field-5                        (field-environment/create
                                        (svg/make-path ["M" fess
                                                        (line/stitch line-base-sinister)
                                                        "L" bend-intersection-sinister
                                                        "L" bottom-right
                                                        "L" base-adjusted
                                                        (line/stitch line-base)
                                                        "z"])
                                        {:parent  field
                                         :context [:per-gyronny :five]})
        field-6                        (field-environment/create
                                        (svg/make-path ["M" base-adjusted
                                                        (line/stitch line-base)
                                                        "L" fess
                                                        (line/stitch line-base-dexter)
                                                        "L" bend-intersection-dexter
                                                        "L" bottom-left
                                                        "z"])
                                        {:parent  field
                                         :context [:per-gyronny :six]})
        field-7                        (field-environment/create
                                        (svg/make-path ["M" fess
                                                        (line/stitch line-base-dexter)
                                                        "L" bend-intersection-dexter
                                                        "L" dexter-adjusted
                                                        (line/stitch line-dexter)
                                                        "z"])
                                        {:parent  field
                                         :context [:per-gyronny :seven]})
        field-8                        (field-environment/create
                                        (svg/make-path ["M" dexter-adjusted
                                                        (line/stitch line-dexter)
                                                        "L" fess
                                                        (line/stitch line-chief-dexter)
                                                        "L" top-left
                                                        "z"])
                                        {:parent  field
                                         :context [:per-gyronny :eight]})]
    [:<>
     [:defs
      [:mask {:id mask-id-1}
       [:path {:d    (:shape field-1)
               :fill "#fff"}]
       [:path.overlap {:d (:shape field-1)}]]
      [:mask {:id mask-id-2}
       [:path {:d    (:shape field-2)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" fess
                            (line/stitch line-chief-sinister)
                            "L" top-right
                            "L" chief-adjusted])}]]
      [:mask {:id mask-id-3}
       [:path {:d    (:shape field-3)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" top-right
                            "L" sinister-adjusted
                            (line/stitch line-sinister)
                            "L" fess])}]]
      [:mask {:id mask-id-4}
       [:path {:d    (:shape field-4)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" fess
                            (line/stitch line-base-sinister)
                            "L" bend-intersection-sinister
                            "L" sinister-adjusted])}]]
      [:mask {:id mask-id-5}
       [:path {:d    (:shape field-5)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" bend-intersection-sinister
                            "L" bottom-right
                            "L" base-adjusted
                            (line/stitch line-base)
                            "L" fess])}]]
      [:mask {:id mask-id-6}
       [:path {:d    (:shape field-6)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" fess
                            (line/stitch line-base-dexter)
                            "L" bend-intersection-dexter
                            "L" bottom-left
                            "L" base-adjusted])}]]
      [:mask {:id mask-id-7}
       [:path {:d    (:shape field-7)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" bend-intersection-dexter
                            "L" dexter-adjusted
                            (line/stitch line-dexter)
                            "L" fess])}]]
      [:mask {:id mask-id-8}
       [:path {:d    (:shape field-8)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" top-left
                            "L" dexter-adjusted])}]]]
     [:g {:mask (str "url(#" mask-id-1 ")")}
      [top-level-render (get-field fields 0) field-1 options]]
     [:g {:mask (str "url(#" mask-id-2 ")")}
      [top-level-render (get-field fields 1) field-2 options]]
     [:g {:mask (str "url(#" mask-id-3 ")")}
      [top-level-render (get-field fields 2) field-3 options]]
     [:g {:mask (str "url(#" mask-id-4 ")")}
      [top-level-render (get-field fields 3) field-4 options]]
     [:g {:mask (str "url(#" mask-id-5 ")")}
      [top-level-render (get-field fields 4) field-5 options]]
     [:g {:mask (str "url(#" mask-id-6 ")")}
      [top-level-render (get-field fields 5) field-6 options]]
     [:g {:mask (str "url(#" mask-id-7 ")")}
      [top-level-render (get-field fields 6) field-7 options]]
     [:g {:mask (str "url(#" mask-id-8 ")")}
      [top-level-render (get-field fields 7) field-8 options]]
     (when (:outline? options)
       [:g.outline
        [:path {:d (svg/make-path
                    ["M" fess
                     (line/stitch line-chief-dexter)])}]
        [:path {:d (svg/make-path
                    ["M" chief-adjusted
                     (line/stitch line-chief)])}]
        [:path {:d (svg/make-path
                    ["M" fess
                     (line/stitch line-chief-sinister)])}]
        [:path {:d (svg/make-path
                    ["M" sinister-adjusted
                     (line/stitch line-sinister)])}]
        [:path {:d (svg/make-path
                    ["M" fess
                     (line/stitch line-base-sinister)])}]
        [:path {:d (svg/make-path
                    ["M" base-adjusted
                     (line/stitch line-base)])}]
        [:path {:d (svg/make-path
                    ["M" fess
                     (line/stitch line-base-dexter)])}]
        [:path {:d (svg/make-path
                    ["M" dexter-adjusted
                     (line/stitch line-dexter)])}]])]))

(defn tierced-per-pale [{:keys [fields line] :as field} environment top-level-render options]
  (let [mask-id-1                      (svg/id "division-tierced-pale-1")
        mask-id-2                      (svg/id "division-tierced-pale-2")
        mask-id-3                      (svg/id "division-tierced-pale-3")
        line-style                     (or (:style line) :straight)
        top-left                       (get-in environment [:points :top-left])
        top-right                      (get-in environment [:points :top-right])
        bottom-left                    (get-in environment [:points :bottom-left])
        bottom-right                   (get-in environment [:points :bottom-right])
        chief                          (get-in environment [:points :chief])
        base                           (get-in environment [:points :base])
        fess                           (get-in environment [:points :fess])
        width                          (:width field)
        col1                           (- (:x fess) (/ width 6))
        col2                           (+ (:x fess) (/ width 6))
        first-chief                    (v/v col1 (:y chief))
        first-base                     (v/v col1 (:y base))
        second-chief                   (v/v col2 (:y chief))
        second-base                    (v/v col2 (:y base))
        {line :line}                   (line/create line-style
                                                    (:y (v/- base chief))
                                                    :flipped? true
                                                    :angle 90)
        {line-reversed        :line
         line-reversed-length :length} (line/create line-style
                                                    (:y (v/- base chief))
                                                    :angle -90
                                                    :reversed? true)
        second-base-adjusted           (v/extend second-chief second-base line-reversed-length)
        field-1                        (field-environment/create
                                        (svg/make-path ["M" first-chief
                                                        (line/stitch line)
                                                        "L" first-base
                                                        "L" bottom-left
                                                        "L" top-left
                                                        "z"])
                                        {:parent  field
                                         :context [:tierced-per-pale :left]})
        field-2                        (field-environment/create
                                        (svg/make-path ["M" first-chief
                                                        (line/stitch line)
                                                        "L" first-base
                                                        "L" second-base-adjusted
                                                        (line/stitch line-reversed)
                                                        "L" second-chief
                                                        "z"])
                                        {:parent  field
                                         :context [:tierced-per-pale :middle]})
        field-3                        (field-environment/create
                                        (svg/make-path ["M" second-base-adjusted
                                                        (line/stitch line-reversed)
                                                        "L" second-chief
                                                        "L" top-right
                                                        "L" bottom-right
                                                        "z"])
                                        {:parent  field
                                         :context [:tierced-per-pale :right]})]
    [:<>
     [:defs
      [:mask {:id mask-id-1}
       [:path {:d    (:shape field-1)
               :fill "#fff"}]
       [:path.overlap {:d (:shape field-1)}]]
      [:mask {:id mask-id-2}
       [:path {:d    (:shape field-2)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" first-base
                            "L" second-base-adjusted
                            (line/stitch line-reversed)
                            "L" second-chief
                            "L" first-chief])}]]
      [:mask {:id mask-id-3}
       [:path {:d    (:shape field-3)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" second-chief
                            "L" top-right
                            "L" bottom-right
                            "L" second-base-adjusted])}]]]
     [:g {:mask (str "url(#" mask-id-1 ")")}
      [top-level-render (get fields 0) field-1 options]]
     [:g {:mask (str "url(#" mask-id-2 ")")}
      [top-level-render (get fields 1) field-2 options]]
     [:g {:mask (str "url(#" mask-id-3 ")")}
      [top-level-render (get fields 2) field-3 options]]
     (when (:outline? options)
       [:g.outline
        [:path {:d (svg/make-path
                    ["M" first-chief
                     (line/stitch line)])}]
        [:path {:d (svg/make-path
                    ["M" second-base-adjusted
                     (line/stitch line-reversed)])}]])]))

(defn tierced-per-fess [{:keys [fields line] :as field} environment top-level-render options]
  (let [mask-id-1                      (svg/id "division-tierced-fess-1")
        mask-id-2                      (svg/id "division-tierced-fess-2")
        mask-id-3                      (svg/id "division-tierced-fess-3")
        line-style                     (or (:style line) :straight)
        top-left                       (get-in environment [:points :top-left])
        top-right                      (get-in environment [:points :top-right])
        bottom-left                    (get-in environment [:points :bottom-left])
        bottom-right                   (get-in environment [:points :bottom-right])
        dexter                         (get-in environment [:points :dexter])
        sinister                       (get-in environment [:points :sinister])
        fess                           (get-in environment [:points :fess])
        height                         (:height field)
        row1                           (- (:y fess) (/ height 6))
        row2                           (+ (:y fess) (/ height 6))
        first-dexter                   (v/v (:x dexter) row1)
        first-sinister                 (v/v (:x sinister) row1)
        second-dexter                  (v/v (:x dexter) row2)
        second-sinister                (v/v (:x sinister) row2)
        {line :line}                   (line/create line-style
                                                    (:x (v/- sinister dexter)))
        {line-reversed        :line
         line-reversed-length :length} (line/create line-style
                                                    (:x (v/- sinister dexter))
                                                    :reversed? true
                                                    :flipped? true
                                                    :angle 180)
        second-sinister-adjusted       (v/extend second-dexter second-sinister line-reversed-length)
        field-1                        (field-environment/create
                                        (svg/make-path ["M" first-dexter
                                                        (line/stitch line)
                                                        "L" first-sinister
                                                        "L" top-right
                                                        "L" top-left
                                                        "z"])
                                        {:parent  field
                                         :context [:tierced-per-fess :top]})
        field-2                        (field-environment/create
                                        (svg/make-path ["M" first-dexter
                                                        (line/stitch line)
                                                        "L" first-sinister
                                                        "L" second-sinister-adjusted
                                                        (line/stitch line-reversed)
                                                        "L" dexter
                                                        "z"])
                                        {:parent  field
                                         :context [:tierced-per-fess :middle]})
        field-3                        (field-environment/create
                                        (svg/make-path ["M" second-sinister-adjusted
                                                        (line/stitch line-reversed)
                                                        "L" dexter
                                                        "L" bottom-left
                                                        "L" bottom-right
                                                        "z"])
                                        {:parent  field
                                         :context [:tierced-per-fess :bottom]})]
    [:<>
     [:defs
      [:mask {:id mask-id-1}
       [:path {:d    (:shape field-1)
               :fill "#fff"}]
       [:path.overlap {:d (:shape field-1)}]]
      [:mask {:id mask-id-2}
       [:path {:d    (:shape field-2)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" first-sinister
                            "L" second-sinister-adjusted
                            (line/stitch line-reversed)
                            "L" dexter
                            "L" first-dexter])}]]
      [:mask {:id mask-id-3}
       [:path {:d    (:shape field-3)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" dexter
                            "L" bottom-left
                            "L" bottom-right
                            "L" second-sinister-adjusted])}]]]
     [:g {:mask (str "url(#" mask-id-1 ")")}
      [top-level-render (get fields 0) field-1 options]]
     [:g {:mask (str "url(#" mask-id-2 ")")}
      [top-level-render (get fields 1) field-2 options]]
     [:g {:mask (str "url(#" mask-id-3 ")")}
      [top-level-render (get fields 2) field-3 options]]
     (when (:outline? options)
       [:g.outline
        [:path {:d (svg/make-path
                    ["M" first-dexter
                     (line/stitch line)])}]
        [:path {:d (svg/make-path
                    ["M" second-sinister-adjusted
                     (line/stitch line-reversed)])}]])]))

(defn tierced-per-pairle [{:keys [fields line] :as field} environment top-level-render options]
  (let [mask-id-1                           (svg/id "division-tierced-pairle-1")
        mask-id-2                           (svg/id "division-tierced-pairle-2")
        mask-id-3                           (svg/id "division-tierced-pairle-3")
        line-style                          (or (:style line) :straight)
        top-left                            (get-in environment [:points :top-left])
        top-right                           (get-in environment [:points :top-right])
        bottom-left                         (get-in environment [:points :bottom-left])
        bottom-right                        (get-in environment [:points :bottom-right])
        base                                (get-in environment [:points :base])
        fess                                (get-in environment [:points :fess])
        {line-chief-dexter        :line
         line-chief-dexter-length :length}  (line/create line-style
                                                         (v/abs (v/- top-left fess))
                                                         :angle 45
                                                         :reversed? true)
        {line-chief-sinister :line}         (line/create line-style
                                                         (v/abs (v/- top-right fess))
                                                         :angle -45
                                                         :flipped? true)
        {line-base :line}                   (line/create line-style
                                                         (v/abs (v/- base fess))
                                                         :flipped? true
                                                         :angle 90)
        {line-base-reversed        :line
         line-base-reversed-length :length} (line/create line-style
                                                         (v/abs (v/- base fess))
                                                         :angle -90
                                                         :reversed? true)
        top-left-adjusted                   (v/extend
                                                fess
                                              top-left
                                              line-chief-dexter-length)
        base-adjusted                       (v/extend
                                                fess
                                              base
                                              line-base-reversed-length)
        field-1                             (field-environment/create
                                             (svg/make-path ["M" top-left-adjusted
                                                             (line/stitch line-chief-dexter)
                                                             "L" fess
                                                             (line/stitch line-chief-sinister)
                                                             "L" top-right
                                                             "z"])
                                             {:parent  field
                                              :context [:tierced-per-pairle :top]})
        field-2                             (field-environment/create
                                             (svg/make-path ["M" fess
                                                             (line/stitch line-chief-sinister)
                                                             "L" top-right
                                                             "L" bottom-right
                                                             "L" base-adjusted
                                                             (line/stitch line-base-reversed)
                                                             "z"])
                                             {:parent  field
                                              :context [:tierced-per-pairle :right]})
        field-3                             (field-environment/create
                                             (svg/make-path ["M" fess
                                                             (line/stitch line-base)
                                                             "L" base
                                                             "L" bottom-left
                                                             "L" top-left-adjusted
                                                             (line/stitch line-chief-dexter)
                                                             "z"])
                                             {:parent  field
                                              :context [:tierced-per-pairle :left]})]
    [:<>
     [:defs
      [:mask {:id mask-id-1}
       [:path {:d    (:shape field-1)
               :fill "#fff"}]
       [:path.overlap {:d (:shape field-1)}]]
      [:mask {:id mask-id-2}
       [:path {:d    (:shape field-2)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" top-right
                            "L" bottom-right
                            "L" base-adjusted
                            (line/stitch line-base-reversed)
                            "L" fess])}]]
      [:mask {:id mask-id-3}
       [:path {:d    (:shape field-3)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" base
                            "L" bottom-left
                            "L" top-left-adjusted])}]]]
     [:g {:mask (str "url(#" mask-id-1 ")")}
      [top-level-render (get fields 0) field-1 options]]
     [:g {:mask (str "url(#" mask-id-2 ")")}
      [top-level-render (get fields 1) field-2 options]]
     [:g {:mask (str "url(#" mask-id-3 ")")}
      [top-level-render (get fields 2) field-3 options]]
     (when (:outline? options)
       [:g.outline
        [:path {:d (svg/make-path
                    ["M" top-left-adjusted
                     (line/stitch line-chief-dexter)])}]
        [:path {:d (svg/make-path
                    ["M" fess
                     (line/stitch line-chief-sinister)])}]
        [:path {:d (svg/make-path
                    ["M" fess
                     (line/stitch line-base)])}]])]))

(defn tierced-per-pairle-reversed [{:keys [fields line] :as field} environment top-level-render options]
  (let [mask-id-1                            (svg/id "division-tierced-pairle-reversed-1")
        mask-id-2                            (svg/id "division-tierced-pairle-reversed-2")
        mask-id-3                            (svg/id "division-tierced-pairle-reversed-3")
        line-style                           (or (:style line) :straight)
        top-left                             (get-in environment [:points :top-left])
        top-right                            (get-in environment [:points :top-right])
        bottom-left                          (get-in environment [:points :bottom-left])
        bottom-right                         (get-in environment [:points :bottom-right])
        chief                                (get-in environment [:points :chief])
        fess                                 (get-in environment [:points :fess])
        bend-intersection-dexter             (v/project top-right fess (:x top-left))
        bend-intersection-sinister           (v/project top-left fess (:x top-right))
        {line-base-sinister        :line
         line-base-sinister-length :length}  (line/create line-style
                                                          (v/abs (v/- bend-intersection-sinister fess))
                                                          :angle -135
                                                          :reversed? true)
        {line-base-dexter :line}             (line/create line-style
                                                          (v/abs (v/- bend-intersection-dexter fess))
                                                          :angle -225
                                                          :flipped? true)
        {line-chief :line}                   (line/create line-style
                                                          (v/abs (v/- chief fess))
                                                          :flipped? true
                                                          :angle -90)
        {line-chief-reversed        :line
         line-chief-reversed-length :length} (line/create line-style
                                                          (v/abs (v/- chief fess))
                                                          :angle 90
                                                          :reversed? true)
        bend-intersection-sinister-adjusted  (v/extend
                                                 fess
                                               bend-intersection-sinister
                                               line-base-sinister-length)
        chief-adjusted                       (v/extend
                                                 fess
                                               chief
                                               line-chief-reversed-length)
        field-1                              (field-environment/create
                                              (svg/make-path ["M" chief-adjusted
                                                              (line/stitch line-chief-reversed)
                                                              "L" fess
                                                              (line/stitch line-base-dexter)
                                                              "L" bend-intersection-dexter
                                                              "L" top-left
                                                              "z"])
                                              {:parent  field
                                               :context [:tierced-per-pairle-reversed :left]})
        field-2                              (field-environment/create
                                              (svg/make-path ["M" fess
                                                              (line/stitch line-chief)
                                                              "L" chief
                                                              "L" top-right
                                                              "L" bend-intersection-sinister-adjusted
                                                              (line/stitch line-base-sinister)
                                                              "z"])
                                              {:parent  field
                                               :context [:tierced-per-pairle-reversed :right]})
        field-3                              (field-environment/create
                                              (svg/make-path ["M" bend-intersection-sinister-adjusted
                                                              (line/stitch line-base-sinister)
                                                              "L" fess
                                                              (line/stitch line-base-dexter)
                                                              "L" bend-intersection-dexter
                                                              "L" bottom-left
                                                              "L" bottom-right
                                                              "z"])
                                              {:parent  field
                                               :context [:tierced-per-pairle-reversed :bottom]})]
    [:<>
     [:defs
      [:mask {:id mask-id-1}
       [:path {:d    (:shape field-1)
               :fill "#fff"}]
       [:path.overlap {:d (:shape field-1)}]]
      [:mask {:id mask-id-2}
       [:path {:d    (:shape field-2)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" chief
                            "L" top-right
                            "L" bend-intersection-sinister-adjusted
                            (line/stitch line-base-sinister)
                            "L" fess])}]]
      [:mask {:id mask-id-3}
       [:path {:d    (:shape field-3)
               :fill "#fff"}]
       [:path.overlap {:d (svg/make-path
                           ["M" bend-intersection-dexter
                            "L" bottom-left
                            "L" bottom-right
                            "L" bend-intersection-sinister-adjusted])}]]]
     [:g {:mask (str "url(#" mask-id-1 ")")}
      [top-level-render (get fields 0) field-1 options]]
     [:g {:mask (str "url(#" mask-id-2 ")")}
      [top-level-render (get fields 1) field-2 options]]
     [:g {:mask (str "url(#" mask-id-3 ")")}
      [top-level-render (get fields 2) field-3 options]]
     (when (:outline? options)
       [:g.outline
        [:path {:d (svg/make-path
                    ["M" fess
                     (line/stitch line-chief)])}]
        [:path {:d (svg/make-path
                    ["M" bend-intersection-sinister-adjusted
                     (line/stitch line-base-sinister)])}]
        [:path {:d (svg/make-path
                    ["M" fess
                     (line/stitch line-base-dexter)])}]])]))

(def kinds
  [["Per Pale" :per-pale per-pale]
   ["Per Fess" :per-fess per-fess]
   ["Per Bend" :per-bend per-bend]
   ["Per Bend Sinister" :per-bend-sinister per-bend-sinister]
   ["Per Chevron" :per-chevron per-chevron]
   ["Per Saltire" :per-saltire per-saltire]
   ["Quarterly" :quarterly quarterly]
   ["Gyronny" :gyronny gyronny]
   ["Tierced per Pale" :tierced-per-pale tierced-per-pale]
   ["Tierced per Fess" :tierced-per-fess tierced-per-fess]
   ["Tierced per Pairle" :tierced-per-pairle tierced-per-pairle]
   ["Tierced per Pairle Reversed" :tierced-per-pairle-reversed tierced-per-pairle-reversed]])

(def kinds-function-map
  (->> kinds
       (map (fn [[_ key function]]
              [key function]))
       (into {})))

(def options
  (->> kinds
       (map (fn [[name key _]]
              [key name]))))

(defn render [{:keys [type] :as division} environment top-level-render options]
  (let [function (get kinds-function-map type)]
    [function division environment top-level-render options]))

(defn mandatory-part-count [type]
  (case type
    nil                          0
    :tierced-per-pale            3
    :tierced-per-fess            3
    :tierced-per-pairle          3
    :tierced-per-pairle-reversed 3
    2))
