(ns pokedex.view.info.info-container
  (:require ["react-native" :as rn]))

(def ^:private container-style
  {:flex 2
   :align-items :center
   :align-self :stretch
   :background-color :white
   :padding 10
   :margin-vertical 10
   :border-radius 15})

(defn- deep-merge [a & maps]
  (if (map? a)
    (apply merge-with deep-merge a maps)
    (apply merge-with deep-merge maps)))

(defn info-container
  "Container view for rendering information boxes. Supports overriding
  default attributes by passing an attribute map as the first argument
  (hiccup style)."
  [& children]
  (if (map? (first children))
    (let [[attrs & child-elems] children]
      (into
       [:> rn/View (deep-merge {:style container-style} attrs)]
       child-elems))
    (into
     [:> rn/View {:style container-style}]
     children)))
