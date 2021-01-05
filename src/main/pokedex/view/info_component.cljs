(ns pokedex.view.info-component
  (:require [react-native :as rn]
            [pokedex.view.image :refer [image]]
            [pokedex.view.util.type :refer [type->colour]]
            [pokedex.subs :as subs]
            [re-frame.core :as rf]))

(def styles
  {:container {:flex 1
               :justify-content :flex-start
               :align-items :center
               :border-radius 15
               :margin 10
               :padding 20}
   :img-view {:flex 1
              :width 150
              :height 150
              :justify-content :center
              :align-items :center}
   :img {:width 150 :height 150}
   :img-face {:background-color :white
              :border-radius (/ 250 2)
              :width 200
              :height 200
              :justify-content :center
              :align-items :center}
   :text {:font-size 22}})

(def key-list
  [:id :name :height :weight :sprites :moves :types])

(defn sub [id]
  @(rf/subscribe [::subs/get-pokemon id key-list]))

(defn modify-style [style-map type]
  (cond-> style-map
    true (assoc :background-color (type->colour type))))

(defn image-face [attrs image-component]
  [:> rn/View attrs 
   image-component])

(defn info-component [{:keys [route]}]
  (let [id (.. route -params -id)
        {:keys [name types sprites]} (sub id)]
    (fn []
      [:> rn/View {:style (modify-style (:container styles)
                                        (first (map (comp :name :type) types)))}
       [image-face {:style (:img-face styles)}
        [image (:img-view styles) (:img styles) sprites]]
       [:> rn/Text {:style (:text styles)}
        (str id " " name)]])))
