(ns pokedex.view.home
  "The root component module where the app navigation logic lives."
  (:require
   ["expo-constants" :as c]
   ["@react-navigation/native" :as n]
   ["@react-navigation/stack" :as stack]
   [pokedex.view.card-list :refer [card-list]]
   [pokedex.view.info.pokemon-info :refer [pokemon-info]]
   [reagent.core :as r]
   [clojure.string :as s]))

(def header-style
  {:header-status-bar-height (.-statusBarHeight c/default)
   :header-style {:background-color :#fa0808
                  :height (+ 40 (.-statusBarHeight c/default))}
   :header-tint-color :black
   :header-title-style {:font-size 25
                        :font-weight :bold
                        :padding-bottom 15}
   :header-left-container-style {:padding-bottom 10}
   :header-title-align :left})

(defonce s (stack/createStackNavigator))

(defonce Navigator (.-Navigator s))
(defonce Screen (.-Screen s))

(defn home []
  [:> n/NavigationContainer
   [:> Navigator {:initial-route-name "Home"
                  :screen-options header-style}
    [:> Screen {:name "Home"
                :component (r/reactify-component card-list)
                :options {:title "Pokédex"}}]
    [:> Screen {:name "Details"
                :component (r/reactify-component pokemon-info)
                :options (fn [^js/Object obj]
                           (let [name (s/capitalize (.. obj -route -params -name))]
                             #js {:title name
                                  :headerTitleAlign :center}))}]]])
