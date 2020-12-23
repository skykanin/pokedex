(ns pokedex.view.info-card
  (:require
   [clojure.string :as s]
   ["react-native" :as rn]))

(def styles
  {:info-card {:flex 1
               :flex-direction :row
               :justify-content :space-between
               :align-items :center
               :background-color :#f9c2ff
               :padding 15
               :margin-vertical 10
               :margin-horizontal 10
               :border-radius 20
               :height 120}
   :info-view {:flex 0.75
               :justify-content :center
               :align-items :flex-start
               :padding 5
               :height 100}
   :txt-view {:flex-grow 1
              :flex-direction :row}
   :name {:font-size 28
          :margin-horizontal 10}
   :id {:font-size 28}
   :type-view {:flex-grow 1
               :flex-direction :row
               :justify-content :space-between
               :align-items :center}
   :small-txt {:font-size 20
               :border-width 2
               :border-color :yellow
               :border-radius 15
               :padding-horizontal 10
               :padding-vertical 3
               :margin-horizontal 5}
   :img {:flex 0.35
         :width 110
         :height 110}})

(defn- render-id [id]
 (cond
  (>= id 100) (str "#" id)
  (>= id 10) (str "#0" id)
  :else (str "#00" id)))

(defn- text-data [id name]
  [:> rn/View {:style (:txt-view styles)}
   [:> rn/Text {:style (:id styles)} (render-id id)]
   [:> rn/Text {:style (:name styles)} (s/capitalize name)]])

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
    {:style {:width 110 :height 110}
     :source {:uri (:front_default sprites)}}]])

(defn info-card [{:keys [id name types sprites]}]
  [:> rn/View {:style (:info-card styles)}
   [:> rn/View {:style (:info-view styles)}
    [text-data id name]
    [type-container types]]
   [image {:style (:img styles)} sprites]])
