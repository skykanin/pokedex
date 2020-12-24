(ns pokedex.view.card-list
  (:require
   [pokedex.view.info-card :refer [info-card]]
   [re-frame.core :as rf]
   ["react-native" :as rn]
   [reagent.core :as r]
   [pokedex.subs :as subs]))

(def style
  {:container {:flex 1}})

(defn render-card [obj]
  (let [item (js->clj (.-item obj) :keywordize-keys true)]
    (r/as-element [info-card item])))

(defn card-list []
  (let [pokemons (rf/subscribe [::subs/get-pokemon])]
    (fn []
      [:> rn/SafeAreaView {:style (:container style)}
       [:> rn/FlatList
        {:data @pokemons
         :get-item-layout
         (fn [_ index]
           (clj->js {:length 120 :offset (* 120 index) :index index}))
         :initial-num-to-render 6
         :render-item render-card
         :key-extractor
         (fn [item-obj] (str (.-id item-obj)))}]])))
