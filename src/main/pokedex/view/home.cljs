(ns pokedex.view.home
  (:require
   ["expo-constants" :as c]
   ["react-native" :as rn]
   [pokedex.view.card-list :refer [card-list]]
   [reagent.core :as r]))

(def styles
  {:home {:flex 1
          :justify-content :center
          :margin-top (.-statusBarHeight c/default)}})

(defn home []
  [:> rn/View {:style (:home styles)}
   (r/as-element [card-list])])
