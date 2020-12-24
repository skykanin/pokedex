(ns pokedex.events
  (:require
   ; [clojure.spec.alpha :as s]
   ; [day8.re-frame.http-fx-alpha]
   [pokedex.db :refer [app-db]]
   [re-frame.core :refer [dispatch reg-event-db reg-event-fx reg-fx after]]
   [superstructor.re-frame.fetch-fx]))

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

(reg-event-fx
 ::fetch-pokemons
 (fn [_ [_ & params]]
   {:fetch
    {:method :get
     :url "https://pokeapi.co/api/v2/pokemon"
     :response-content-types {#"application/.*json" :json}
     :params (apply hash-map params)
     :on-success [:fetch-pokemon]}}))

(defn create-req [url]
  [:fetch
   {:method :get
    :url url
    :response-content-types {#"application/.*json" :json}
    :on-success [:insert-pokemon]
    :on-failure [:log]}])

(reg-event-fx
 :log
 (fn [_ [_ to-log]]
   {:pprint to-log}))

(reg-fx
 :pprint
 (fn [to-print]
   (cljs.pprint/pprint to-print)))

(reg-event-fx
 :fetch-pokemon
 (fn [_ [_ {:keys [body]}]]
   {:fx (mapv (comp create-req :url) (:results body))}))

(reg-event-db
 :insert-pokemon
 (fn [db [_ response]]
   (let [pokemon (:body response)]
     (update db :list conj pokemon))))
