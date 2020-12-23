(ns pokedex.app
  (:require
   [pokedex.view.home :refer [home]]
   [reagent.core :as r]
   [re-frame.core :as rf]
   [shadow.expo :as expo]
   [pokedex.events :as events]))

(defn start
  {:dev/after-load true}
  []
  (expo/render-root (r/as-element [home #_{:x (js/Date.now)}])))

(defn init []
  (rf/dispatch-sync [::events/initialise-db])
  (rf/dispatch [::events/fetch-pokemons :limit 80 :offset 500])
  (start))
