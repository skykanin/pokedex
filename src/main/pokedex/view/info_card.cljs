(ns pokedex.view.info-card
  (:require
   [clojure.string :as s]
   [pokedex.view.image :refer [image]]
   [pokedex.view.util.type :refer [type->colour]]
   ["react-native" :as rn]
   [re-frame.core :as rf]
   [pokedex.events :as events]))

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

(defn modify-style [index type length style-map]
  (cond-> style-map
    (zero? index) (assoc :margin-top 10)
    (= index (dec length)) (assoc :margin-bottom 10)
    true (assoc :background-color (type->colour type))))

(defn info-card [{:keys [id index length name navigation types sprites]}]
  [:> rn/TouchableOpacity
   {:on-press (fn []
                (rf/dispatch [::events/fetch-pokemon-specie id])
                (. navigation push "Details" #js {:id id :name name}))}
   [:> rn/View {:style (modify-style index
                                     (first (map (comp :name :type) types))
                                     length
                                     (:info-card styles))}
    [:> rn/View {:style (:info-view styles)}
     [text-data id name]
     [type-container types]]
    [image (:img-view styles) (:img styles) sprites]]])
