(ns pokedex.view.info-card
  (:require
   [clojure.string :as s]
   ["react-native" :as rn]))

(def styles
  {:info-card {:flex 1
               :flex-direction :row
               :justify-content :space-between
               :align-items :center
               :padding 15
               :margin-horizontal 10
               :border-radius 15
               :height 100}
   :info-view {:flex 0.75
               :justify-content :center
               :align-items :flex-start
               :padding 5
               :height 100}
   :txt-view {:flex-grow 0.3
              :flex-direction :row
              :flex-wrap :wrap
              :justify-content :flex-start
              :align-items :center
              :margin-top 5}
   :name {:font-size 20}
   :id {:font-size 16
        :opacity 0.5
        :margin-left 5}
   :type-view {:flex-grow 1
               :flex-direction :row
               :justify-content :space-between
               :align-items :center}
   :small-txt {:font-size 16
               :border-width 2
               :border-radius 10
               :opacity 0.6
               :padding-horizontal 10
               :padding-vertical 3
               :margin-right 5}
   :img-view {:flex 0.25
              :width 100
              :height 100
              :justify-content :center
              :align-items :center}
   :img (let [safepad 10]
          {:width (- 100 safepad)
           :height (- 100 safepad)})})

(def type->colour
  {"unknown" "#68a090"
   "bug" "#a8b820"
   "dark" "#705848"
   "dragon" "#7038f8"
   "electric" "#f8d030"
   "fairy" "#ee99ac"
   "fighting" "#c03028"
   "fire" "#f08030"
   "flying" "#a890f0"
   "ghost" "#705898"
   "grass" "#78c850"
   "ground" "#e0c068"
   "ice" "#98d8d8"
   "normal" "#a8a878"
   "poison" "#a040a0"
   "psychic" "#f85888"
   "rock" "#b8a038"
   "steel" "#b8b8d0"
   "water" "#6890f0"})

(defn- render-id [id]
 (cond
  (>= id 100) (str "#" id)
  (>= id 10) (str "#0" id)
  :else (str "#00" id)))

(defn- text-data [id name]
  [:> rn/View {:style (:txt-view styles)}
   [:> rn/Text {:style (:name styles)} (s/capitalize name)]
   [:> rn/Text {:style (:id styles)} (render-id id)]])

(defn- type-container [types]
  (->> types
       (map (fn [t]
              [:> rn/Text
               {:style (:small-txt styles)}
               (:name (:type t))]))
       (into [:> rn/View
              {:style (:type-view styles)}])))

(defn- image [style sprites]
  [:> rn/View style
   [:> rn/Image
    {:style (:img styles)
     :source {:uri (:front_default sprites)}}]])

(defn modify-style [index type length style-map]
  (cond-> style-map
    (zero? index) (assoc :margin-top 10)
    (= index (dec length)) (assoc :margin-bottom 10)
    true (assoc :background-color (type->colour type))))

(defn info-card [{:keys [id index length name types sprites]}]
  [:> rn/View {:style (modify-style index
                                 (first (map (comp :name :type) types))
                                 length
                                 (:info-card styles))}
   [:> rn/View {:style (:info-view styles)}
    [text-data id name]
    [type-container types]]
   [image {:style (:img-view styles)} sprites]])
