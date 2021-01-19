(ns pokedex.view.info.base-stats
  (:require [pokedex.view.info.info-container :refer [info-container]]
            [pokedex.view.info.util :refer [spacer-bar]]
            [pokedex.view.util.type :refer [modify-style]]
            ["react-native" :as rn]
            [clojure.string :as s]))

(def ^:private styles
  {:bar-view {:flex 1
              :flex-direction :row
              :justify-content :flex-end
              :align-items :center
              :padding 10
              :background-color :pink
              :height 25
              :border-radius 5}
   :bars-view {:flex 2}
   :column-view {:flex 1}
   :text {:font-size 15}
   :space 5})

(defn- flatten-stats
  "Flatten stat maps inside the stats vector."
  [stats]
  (letfn [(f [{stat :stat :as m}]
            (merge m stat))]
    (map f stats)))

(defn- calc-max [stats]
  (apply max (map :base_stat stats)))

(defn- calc-percent [value maximum]
  (str (* 100 (/ value maximum)) "%"))

(defn- update-width [style-map value maximum]
  (assoc style-map :width (calc-percent value maximum)))

(defn- shorten [word]
  (as-> word w
      (s/replace w #"Special-" "Sp. ")
      (s/split w #" ")
      (map s/capitalize w)
      (s/join " " w)))

(defn- stat-bar
  "Individual stat bar component."
  [type highest {:keys [base_stat]}]
  [:> rn/View {:style (-> (:bar-view styles)
                          (update-width base_stat highest)
                          (modify-style type))}
   [:> rn/Text {:style (:text styles)} (str base_stat)]])

(defn- info-column
  "Column for the stat names."
  [names]
  (into [:> rn/View {:style (:column-view styles)}]
        (interpose [spacer-bar (* 2.2 (:space styles))]
                   (map (fn [name]
                          [:> rn/Text (shorten (s/capitalize name))]) names))))

(defn- bar-column
  "Column for the stat bars."
  [type highest statvec]
  (into [:> rn/View {:style (:bars-view styles)}]
        (interpose [spacer-bar (:space styles)]
                   (map (partial stat-bar type highest) statvec))))

(defn base-stats-info
  "Base stats component."
  [type stats]
  (let [statvec (flatten-stats stats)
        highest (calc-max statvec)]
    [info-container {:style {:align-items :flex-start
                             :flex-direction :row}}
     [info-column (map :name statvec)]
     [bar-column type highest statvec]]))
