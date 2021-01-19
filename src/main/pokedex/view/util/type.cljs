(ns pokedex.view.util.type)

(def type->colour
  {"unknown" "#68a090"
   "bug" "#a8b820"
   "dark" "#705848"
   "dragon" "#7038f8"
   "electric" "#f8d030"
   "fairy" "#ee99ac"
   "fighting" "#c03028"
   "fire" "#f08030"
   "flying" "#a890f0"
   "ghost" "#705898"
   "grass" "#78c850"
   "ground" "#e0c068"
   "ice" "#98d8d8"
   "normal" "#a8a878"
   "poison" "#a040a0"
   "psychic" "#f85888"
   "rock" "#b8a038"
   "steel" "#b8b8d0"
   "water" "#6890f0"})

(defn modify-style [style-map type]
  (assoc style-map :background-color (type->colour type)))
