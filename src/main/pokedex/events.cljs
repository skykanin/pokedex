(ns pokedex.events
  (:require-macros [cljs.core.async :refer [go]])
  (:require
   [cljs.core.async :refer [<! take!]]
   [cljs-http.client :as http]
   ; [clojure.spec.alpha :as s]
   ; [day8.re-frame.http-fx-alpha]
   [pokedex.db :refer [app-db]]
   [re-frame.core :refer [dispatch reg-event-db reg-event-fx reg-fx after]]))

;; -- Interceptors ------------------------------------------------------------
;;
;; See https://github.com/Day8/re-frame/blob/master/docs/Interceptors.md
;;
;; (defn check-and-throw
;;   "Throw an exception if db doesn't have a valid spec."
;;   [spec db [event]]
;;   (when-not (s/valid? spec db)
;;     (let [explain-data (s/explain-data spec db)]
;;       (throw (ex-info (str "Spec check after " event " failed: " explain-data) explain-data))]))
;;
;; (def validate-spec
;;   (if goog.DEBUG
;;     (after (partial check-and-throw ::db/app-db))
;;     []]))

;; -- Handlers --------------------------------------------------------------

(reg-event-db
 ::initialise-db
 (fn [_ _]
   app-db))

(reg-event-db
 ::add-pokemon
 (fn [db [_ pokemon]]
   (update db :list conj pokemon)))

(reg-event-fx
 ::fetch-pokemons
 (fn [_ [_ & params]]
   {:fetch-pokemons (apply hash-map params)}))

(defn fetch-pokemons [params]
  (go
    (let [result (<! (http/get "https://pokeapi.co/api/v2/pokemon"
                               {:query-params params}))
          data (:results (:body result))
          callback #(dispatch [::add-pokemon (:body %)])]
      (doseq [url (map :url data)]
        (take! (http/get url) callback)))))

(reg-fx
 :fetch-pokemons
 (fn [query-params]
   (fetch-pokemons query-params)))
