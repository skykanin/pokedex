(ns pokedex.view.info.species-info
  (:require [pokedex.view.info.info-container :refer [info-container]]
            ["react-native" :as rn]
            [clojure.string :as s]))

(def ^:private styles
  {:species-h1 {:font-size 18
                :padding-vertical 5
                :padding-horizontal 10}
   :species-h2 {:font-size 14
                :color :#333333
                :border-width 0
                :border-radius 10
                :padding 5
                :text-align :center}})

(def ^:private species-h1 (:species-h1 styles))
(def ^:private species-h2 (:species-h2 styles))

(defn- with-title [title t-style & components]
  (into [:> rn/View {:style {:flex 1 :align-items :center}}
         [:> rn/Text {:style t-style} title]]
        components))

(defn- strip-whitespace [txt]
  (as-> txt t
      (s/split t #"\s")
      (filter seq t)
      (s/join " " t)))

(defn- get-en-text
  "Get first flavour text in English."
  [flavour-text-entries]
  (->> flavour-text-entries
       (filter (comp #{"en"} :name :language))
       (map :flavor_text)
       first
       strip-whitespace))

(defn species-info
  "Species info component which dispays pokémon
  flavour text, height and width."
  [flavour-text-entries weight height]
  [info-container
   [with-title "Pokédex Entry" species-h1
    [:> rn/Text {:style species-h2}
     (get-en-text flavour-text-entries)]]
   [:> rn/View {:style {:flex 1 :flex-direction :row
                        :align-items :center}}
    [with-title "Height" species-h1
     [:> rn/Text {:style species-h2}
      (str (/ height 10) "m")]]
    [with-title "Weight" species-h1
     [:> rn/Text {:style species-h2}
      (str (/ weight 10) "kg")]]]])
