(ns pokedex.view.image
  (:require ["react-native" :as rn]))

(defn image [view-style img-style sprites]
  [:> rn/View {:style view-style}
   [:> rn/Image
    {:style img-style
     :source {:uri (:front_default sprites)}}]])
