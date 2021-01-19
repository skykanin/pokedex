(ns pokedex.view.info.util
  "Utility components module"
  (:require ["react-native" :as rn]))

(defn spacer-bar [height]
  [:> rn/View {:style {:height height}}])
