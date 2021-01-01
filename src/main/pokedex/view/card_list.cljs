(ns pokedex.view.card-list
  (:require
   [pokedex.view.info-card :refer [info-card]]
   [re-frame.core :as rf]
   ["react-native" :as rn]
   [reagent.core :as r]
   [pokedex.subs :as subs]))

(def styles
  {:separator {:height 10
               :width "100%"}})

(defn render-card [obj]
  (let [item (.-item obj)]
    (r/as-element [info-card (assoc item :index (.-index obj))])))

(defn- seperator []
  [:> rn/View {:style (:separator styles)}])

(defn- convert-to-array [vec]
  (let [arr #js []]
    (doseq [x vec]
      (.push arr x))
    arr))

(defn card-list []
  (r/create-element
    rn/FlatList
    #js {:data (convert-to-array
                @(rf/subscribe
                  [::subs/get-pokemon [:id :name :types :sprites]]))
         :getItemLayout (fn [_ index]
                          #js {:length 100
                               :offset (* 100 index) :index index})
         :initialNumToRender 7
         :ItemSeparatorComponent (r/reactify-component seperator)
         :keyExtractor (fn [item-obj _] (str (:id item-obj)))
         :progressViewOffset true
         :renderItem render-card}))
