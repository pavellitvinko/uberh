(ns helicopter_rent.repositories.helicopters
  (:use [helicopter_rent.config :refer :all]
        [clojure.java.jdbc :as jdbc])
  (:gen-class))

(defn get-all []
  (jdbc/query db ["SELECT * FROM helicopters"]))

(defn get_by_id [id]
  (let [m (first (jdbc/query db ["SELECT * FROM helicopters WHERE id = ?" id]))]
    {:id   (:id m)
     :name (:name m)
     :description (:description m)
     :service_class (:service_class m)
     :location {:location_x m
                :location_y m}}))

(defn delete [id]
  (jdbc/delete! db :helicopters ["id = ?" id]))

(defn update-location [id point]
  (jdbc/update! db :helicopters {:location_x (:x point) :location_y (:y point)} ["id = ?" id]))

(defn create [name description service_class location]
  (jdbc/insert! db :helicopters {:name          name
                                 :description   description
                                 :service_class service_class
                                 :location_x    (:x location)
                                 :location_y    (:y location)}))
                        
(defn get-free [service_class]
  (jdbc/query db ["SELECT * FROM helicopters"]))
