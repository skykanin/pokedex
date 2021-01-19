(ns pokedex.view.info.base-stats
  (:require [pokedex.view.info.info-container :refer [info-container]]
            [pokedex.view.info.util :refer [spacer-bar]]
            [pokedex.view.util.type :refer [modify-style]]
            ["react-native" :as rn]
            [clojure.string :as s]))

(def ^:private styles
  {:bar-view {:flex 1
              :flex-direction :row
              :justify-content :space-between
              :align-items :center
              :padding 10
              :background-color :pink
              :height 25
              :border-radius 5}
   :text {:font-size 15}})

(defn- flatten-stats
  "Flatten stat maps inside the stats vector."
  [stats]
  (letfn [(f [{stat :stat :as m}]
            (merge m stat))]
    (map f stats)))

(defn- calc-max [stats]
  (apply max (map :base_stat stats)))

(defn- calc-percent [value maximum]
  (* 300 (/ value maximum)))

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
  [type highest {:keys [base_stat name]}]
  [:> rn/View {:style (-> (:bar-view styles)
                          (update-width base_stat highest)
                          (modify-style type))}
   [:> rn/Text {:style (:text styles)} (shorten (s/capitalize name))]
   [:> rn/Text {:style (:text styles)} base_stat]])

(defn base-stats-info [type stats]
  (let [statvec (flatten-stats stats)
        highest (calc-max statvec)]
    (into [info-container {:style {:align-items :flex-start}}]
          (interpose [spacer-bar 5]
                     (map (partial stat-bar type highest) statvec)))))
