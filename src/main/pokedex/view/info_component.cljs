(ns pokedex.view.info-component
  (:require [react-native :as rn]
            [pokedex.view.image :refer [image]]
            [pokedex.view.util.type :refer [type->colour]]
            [pokedex.subs :as subs]
            [re-frame.core :as rf]
            [clojure.string :as str]))

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
   :species-view {:flex 2
                  :justify-content :space-between
                  :align-items :center
                  :height 200
                  :background-color :white
                  :padding 10
                  :margin-vertical 10
                  :border-radius 15}
   :abilities-view {:flex 2
                    :height 200
                    :background-color :white
                    :padding 10
                    :margin-top 10
                    :border-radius 15
                    :alignSelf :stretch}
   :ability-bar-view {:flex-direction :row
                      :align-items :center
                      :border-radius 12
                      :border-width 1
                      :height 40
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
                   :width "100%"}
   :species-h1    {:font-size 18
                   :padding-vertical 5
                   :padding-horizontal 10}
   :species-h2 {:font-size 16
                :border-width 1
                :border-radius 10
                :padding 5}})

(def key-list
  [:abilities :id :name :height :weight :sprites :moves :types])

(defn sub [id]
  @(rf/subscribe [::subs/get-pokemon id key-list]))

(defn modify-style [style-map type]
  (cond-> style-map
    true (assoc :background-color (type->colour type))))

(defn image-face [attrs image-component]
  [:> rn/View attrs image-component])

(defn with-title [title t-style & components]
  (into [:> rn/View {:style {:flex 1 :align-items :center}}
         [:> rn/Text {:style t-style} title]]
        components))

(def species-h1 (:species-h1 styles))
(def species-h2 (:species-h2 styles))

(defn species-info [view-style weight height]
  [:> rn/View view-style
   [with-title "Pokédex Entry" species-h1
    [:> rn/Text {:style species-h2}
     "Flavour text for best effect with rolling text. You can't regret the best select of flavour text."]]
   [:> rn/View {:style {:flex 1 :flex-direction :row
                        :align-items :center}}
    [with-title "Height" species-h1
     [:> rn/Text {:style species-h2}
      (str (/ height 10) "m")]]
    [with-title "Weight" species-h1
     [:> rn/Text {:style species-h2}
      (str (/ weight 10) "kg")]]]])

(defn render-ability [name]
  (str/join " " (map str/capitalize (str/split name #"-"))))

(defn ability-bar [type {:keys [is_hidden ability]}]
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

(defn abilities-info [type abilities]
  (into [:> rn/View {:style (:abilities-view styles)}]
        (map (partial ability-bar type) abilities)))

(defn info-component [{:keys [route]}]
  (let [id (.. route -params -id)
        {:keys [abilities types sprites weight height]} (sub id)]
    (fn []
      (let [type (first (map (comp :name :type) types))]
        [:> rn/View {:style (modify-style (:container styles) type)}
         [image-face {:style (:img-face styles)}
          [image (:img-view styles) (:img styles) sprites]]
         [species-info (:species-view styles) weight height]
         [abilities-info type abilities]]))))
