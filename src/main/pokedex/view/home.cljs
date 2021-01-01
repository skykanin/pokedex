(ns pokedex.view.home
  (:require
   ["expo-constants" :as c]
   ["@react-navigation/native" :as n]
   ["@react-navigation/stack" :as stack]
   [pokedex.view.card-list :refer [card-list]]
   [reagent.core :as r]))

(def header-style
  {:header-status-bar-height (.-statusBarHeight c/default)
   :header-style {:background-color :#fa0808
                  :height (+ 30 (.-statusBarHeight c/default))}
   :header-tint-color :black
   :header-title-style {:font-size 25
                        :font-weight :bold}
   :header-title-container-style {:align-items :flex-start
                                  :padding-bottom 15}})

(defonce s (stack/createStackNavigator))

(defonce Navigator (.-Navigator s))
(defonce Screen (.-Screen s))

(defn home []
  [:> n/NavigationContainer
   [:> Navigator {:initial-route-name "Home"
                  :screen-options header-style}
    [:> Screen {:name "Home"
                :component (r/reactify-component card-list)
                :options {:title "Pokédex"}}]]])
