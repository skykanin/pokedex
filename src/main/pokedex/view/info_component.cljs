(ns pokedex.view.info-component
  (:require [pokedex.subs :as subs]
            [pokedex.view.image :refer [image]]
            [pokedex.view.info.abilities-info :refer [abilities-info]]
            [pokedex.view.info.base-stats :refer [base-stats-info]]
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
              :align-items :center}
   :info-list {:width "100%"}})

(def ^:private pokemon-keys
  [:abilities :id :name :height :weight :sprites :stats :moves :types])

(def ^:private specie-keys
  [:flavor_text_entries])

(defn- sub-pokemon [id]
  @(rf/subscribe [::subs/get-pokemon id pokemon-keys]))

(defn- sub-specie [id]
  @(rf/subscribe [::subs/get-specie id specie-keys]))

(defn- image-face [attrs image-component]
  [:> rn/View attrs image-component])

(defn info-component [{:keys [route]}]
  (fn []
    (let [id (.. route -params -id)
          {:keys [abilities types sprites stats weight height]} (sub-pokemon id)
          {flavour-text-entries :flavor_text_entries} (sub-specie id)
          type (first (map (comp :name :type) types))]
     [:> rn/View {:style (modify-style (:container styles) type)}
      [image-face {:style (:img-face styles)}
       [image (:img-view styles) (:img styles) sprites]]
      [:> rn/ScrollView {:style (:info-list styles)}
       [species-info flavour-text-entries weight height]
       [abilities-info type abilities]
       [base-stats-info type stats]]])))
