(ns pokedex.view.info.species-info
  (:require [pokedex.view.info.info-container :refer [info-container]]
            ["react-native" :as rn]))

(def ^:private styles
  {:species-h1 {:font-size 18
                :padding-vertical 5
                :padding-horizontal 10}
   :species-h2 {:font-size 16
                :border-width 1
                :border-radius 10
                :padding 5}})

(def ^:private species-h1 (:species-h1 styles))
(def ^:private species-h2 (:species-h2 styles))

(defn- with-title [title t-style & components]
  (into [:> rn/View {:style {:flex 1 :align-items :center}}
         [:> rn/Text {:style t-style} title]]
        components))

(defn species-info
  "Species info component which dispays pokémon
  flavour text, height and width."
  [weight height]
  [info-container 
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
