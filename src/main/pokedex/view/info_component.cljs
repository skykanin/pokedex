(ns pokedex.view.info-component
  (:require [pokedex.subs :as subs]
            [pokedex.view.image :refer [image]]
            [pokedex.view.info.abilities-info :refer [abilities-info]]
            [pokedex.view.info.species-info :refer [species-info]]
            [pokedex.view.util.type :refer [modify-style]]
            ["react-native" :as rn]
            [re-frame.core :as rf]))

(def styles
  {:container {:flex 1
               :justify-content :flex-start
               :align-items :center
               :border-radius 15
               :margin 10
               :padding 15}
   :img-view {:flex 1
              :width 125
              :height 125
              :justify-content :center
              :align-items :center}
   :img {:width 150 :height 150}
   :img-face {:background-color :white
              :border-radius (/ 175 2)
              :width 175
              :height 175
              :justify-content :center
              :align-items :center}})

(def key-list
  [:abilities :id :name :height :weight :sprites :moves :types])

(defn sub [id]
  @(rf/subscribe [::subs/get-pokemon id key-list]))

(defn image-face [attrs image-component]
  [:> rn/View attrs image-component])

(defn info-component [{:keys [route]}]
  (let [id (.. route -params -id)
        {:keys [abilities types sprites weight height]} (sub id)]
    (fn []
      (let [type (first (map (comp :name :type) types))]
        [:> rn/View {:style (modify-style (:container styles) type)}
         [image-face {:style (:img-face styles)}
          [image (:img-view styles) (:img styles) sprites]]
         [species-info weight height]
         [abilities-info type abilities]]))))
