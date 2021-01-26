(ns pokedex.view.skeleton
  "Skeleton loading component module"
  (:require ["react-native" :as rn]
            [pokedex.view.info.util :refer [spacer-bar]]))

;; Default styling for the skeleton root view
(def ^:private default
  {:flex 1
   :padding 10
   :border-radius 15})

;; Default styling for the skeleton bars
(def ^:private bar-default
  {:border-radius 8
   :width "60%"
   :height 20})

(defn- bars [style count spacing]
  (interpose
   [spacer-bar spacing]
   (repeat count
           [:> rn/View
            {:style (merge bar-default style)}])))
  
(defn skeleton
  [{:keys [style bar-style bar-count bar-spacing]}]
  (into
   [:> rn/View {:style (merge default style)}]
   (bars bar-style bar-count bar-spacing)))
