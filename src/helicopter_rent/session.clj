(ns helicopter_rent.session
  (:require [helicopter_rent.repositories.users :as users]))

(def connections_atom (atom {}))

(defn add [login password]
  (do
    (if (not-empty (users/get_by_password login password))
      (do
        (swap! connections_atom conj {(keyword (str (hash login))) {:login login :starttime (System/currentTimeMillis)}})
        (println connections_atom)
        (str "{\"token\":\"" (str (hash login)) "\", \"id\":\"" (str (get (first (users/get_by_password login password)) :id)) "\"}"))
      (do
        ("")))))

(defn remove [hash]
  (swap! connections_atom dissoc (keyword hash))
  (println connections_atom))

(defn exists [hash]
  (do
      (and
        (get @connections_atom (keyword hash))
        (< (- (System/currentTimeMillis) (get (get @connections_atom (keyword hash)) :starttime)) 3600000))))
