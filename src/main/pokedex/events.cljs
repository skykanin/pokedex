(ns pokedex.events
  (:require
   [cljs.pprint :refer [pprint]]
   [pokedex.db :refer [app-db]]
   [re-frame.core :refer [reg-event-db reg-event-fx reg-fx]]
   [superstructor.re-frame.fetch-fx]))

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
   (pprint to-print)))

(reg-event-fx
 :fetch-pokemon
 (fn [_ [_ {:keys [body]}]]
   {:fx (mapv (comp create-req :url) (:results body))}))

(reg-event-db
 :insert-pokemon
 (fn [db [_ response]]
   (let [pokemon (:body response)]
     (update db :list conj pokemon))))
