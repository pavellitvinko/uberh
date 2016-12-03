(ns helicopter_rent.repositories.orders
  (:use [helicopter_rent.config :refer :all]
        [clojure.java.jdbc :as jdbc]
        [helicopter_rent.models.status :as status])
  (:gen-class))

(defn get-all []
  (jdbc/query db ["SELECT * FROM orders"]))

(defn get_by_id [id]
  (let [m (first (jdbc/query db ["SELECT * FROM orders WHERE id = ?" id]))]
    {:id            (:id m)
     :user_id       (:user_id m)
     :helicopter_id (:helicopter_id m)
     :date          (:date m)
     :status        (:status m)
     :start_point   {:start_x m
                     :start_y m}
     :end_point     {:end_x m
                     :end_y m}}))

(defn delete [id]
  (jdbc/delete! db :orders ["id = ?" id]))

(defn set-status [id new-status]
  (jdbc/update! db :orders {:status new-status} ["id = ?" id]))

(defn set-start-date [id start-date]
  (jdbc/update! db :orders {:start_date start-date} ["id = ?" id]))

(defn set-finish-date [id finish-date]
  (jdbc/update! db :orders {:finish_date finish-date} ["id = ?" id]))

(defn set-end-point [id point]
  (jdbc/update! db :orders {:end_x (:x point) :end_y (:y point)} ["id = ?" id]))

(defn set-price [id price]
  (jdbc/update! db :orders {:price price} ["id = ?" id]))

(defn create [user_id helicopter_id start_point date]
  (:generated_key
    (first
      (jdbc/insert! db :orders {:user_id       user_id
                                :helicopter_id helicopter_id
                                :start_x       (:x start_point)
                                :start_y       (:y start_point)
                                :date          date
                                :status        (:submitted status)}))))
