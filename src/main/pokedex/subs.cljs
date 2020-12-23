(ns pokedex.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 ::get-pokemon
 (fn [db _]
   (when-let [pokemons (seq (:list db))]
     (sort-by :id pokemons))))
