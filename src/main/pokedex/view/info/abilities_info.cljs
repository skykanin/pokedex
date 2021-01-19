(ns pokedex.view.info.abilities-info
  (:require [clojure.string :as s]
            [pokedex.view.info.info-container :refer [info-container]]
            [pokedex.view.util.type :refer [modify-style]]
            ["react-native" :as rn]))

(def ^:private styles
  {:ability-bar-view {:flex-direction :row
                      :align-items :center
                      :border-radius 12
                      :border-width 1
                      :height 35
                      :margin-bottom 10}
   :ability-text {:font-size 18
                  :text-align :center
                  :text-align-vertical :center}
   :hidden-style  {:flex 1
                   :border-color :black
                   :border-right-width 1
                   :border-top-left-radius 12
                   :border-bottom-left-radius 12
                   :justify-content :center
                   :background-color :white
                   :font-size 16
                   :padding-horizontal 10
                   :text-align :center
                   :text-align-vertical :center}
   :ability-cover {:flex 1
                   :align-self :stretch
                   :justify-content :center
                   :width "100%"}})

(defn- render-ability [name]
  (s/join " " (map s/capitalize (s/split name #"-"))))

(defn- ability-bar [type {:keys [is_hidden ability]}]
  (letfn [(alter-background [m] (cond-> m is_hidden (assoc :background-color :white)))
          (add-border-right [m v] (merge m {:border-top-right-radius v
                                            :border-bottom-right-radius v}))]
    [:> rn/View {:style (modify-style (:ability-bar-view styles) type)}
     [:> rn/View {:style (:ability-cover styles)}
      (when is_hidden
        [:> rn/Text {:style (modify-style (:hidden-style styles) type)}
         "Hidden"])]
     [:> rn/View {:style (alter-background
                          (update (:ability-cover styles) :flex inc))}
      [:> rn/Text {:style (:ability-text styles)}
       (render-ability (:name ability))]]
     [:> rn/View {:style (alter-background
                          (add-border-right
                           (:ability-cover styles) 12))}]]))

(defn abilities-info
  "Abilities information component which renders pokémons abilities"
  [type abilities]
  (into [info-container]
        (map (partial ability-bar type) abilities)))
