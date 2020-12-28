(ns pokedex.view.home
  (:require
   ["expo-constants" :as c]
   ["react-native" :as rn]
   ["@react-navigation/native" :as n]
   ["@react-navigation/stack" :as stack]
   [pokedex.view.card-list :refer [card-list]]
   [reagent.core :as r]))

(def styles
  {:home {:flex 1
          :justify-content :center
          :margin-top (.-statusBarHeight c/default)}})

; (defn home []
;   [:> rn/View {:style (:home styles)}
;    (r/as-element [card-list])})

(defn one [{:keys [navigation]}]
  ; (println (.-navigate navigation))
  [:> rn/View {:style {:flex 1 :align-items :center :justify-content :center}}
   [:> rn/Text "hello from one"]
   [:> rn/Button {:title "Go to other screen"
                  :on-press (fn [] (. navigation navigate "Other"))}]])

(defn two []
  [:> rn/Text "hello from two"])

(defonce s (stack/createStackNavigator))

(defonce Navigator (.-Navigator s))
(defonce Screen (.-Screen s))

(defn home []
  [:> n/NavigationContainer
   [:> Navigator {:initial-route-name "Home"}
    [:> Screen {:name "Home"
                :component (r/reactify-component one)
                :options {:title "First"}}]
    [:> Screen {:name "Other"
                :component (r/reactify-component two)
                :options {:title "Second"}}]]])
