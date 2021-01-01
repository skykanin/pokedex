(ns pokedex.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 ::get-pokemon
 (fn [db [_ keys]]
   (when-let [pokemons (seq (:list db))]
     (->> pokemons
          (sort-by :id)
          (map #(-> %
                    (select-keys keys)
                    (assoc :length (count pokemons))))))))
