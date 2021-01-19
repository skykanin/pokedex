(ns pokedex.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 ::get-pokemons
 (fn [db [_ keys]]
   (when-let [pokemons (seq (:pokemons db))]
     (->> pokemons
          (sort-by :id)
          (map #(-> %
                    (select-keys keys)
                    (assoc :length (count pokemons))))))))

(defn lookup-id [id maps]
  (first (filter (comp #{id} :id) maps)))

(reg-sub
 ::get-pokemon
 (fn [db [_ id keys]]
   (when-let [pokemon (lookup-id id (:pokemons db))]
     (select-keys pokemon keys))))

(reg-sub
 ::get-specie
 (fn [db [_ id keys]]
   (when-let [specie (lookup-id id (:species db))]
     (select-keys specie keys))))
