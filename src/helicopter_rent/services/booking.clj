(ns helicopter_rent.services.booking
  (:use [helicopter_rent.models.status :as status])
  (:require [clj-time.local :as l]
            [clj-time.jdbc]
            [helicopter_rent.repositories.orders :as orders]
            [helicopter_rent.repositories.users :as users]
            [helicopter_rent.repositories.helicopters :as helicopters]))

(defn calc-distance [a b]
  1)                                                        ; TODO: do actual calculation

(defn calc-flight-time [a b]
  (let [distance (calc-distance a b)]
    (/ distance 150)))                                      ; TODO: move const 'av_speed' to separate file

(defn find_nearest_helicopter [location service_class]
  (if-let [helicopters (helicopters/get-free service_class)]
    (first (sort-by
             (fn [h]
               (calc-distance (:location h) location))
             helicopters))))

(defn get-price-multiplier [service_class]
  1)                                                        ; TODO: do actual calculation

(defn calc-price [date start_point end_point service_class]
  (let [flight-time (calc-flight-time start_point end_point)]
    flight-time))                                           ; TODO: do actual calculation

(defn book_helicopter [user_id start_point date service_class]
  (if-let [user (users/get-by-id user_id)]
    ; user exist
    (if-let [helicopter (find_nearest_helicopter start_point service_class)]
      ; helicopter found
      (orders/create user_id (:id helicopter) start_point date)
      "free helicopter not found")
    "user not found"))

(defn start_trip [order_id helicopter_id]
  (if-let [helicopter (helicopters/get-by-id helicopter_id)]
    ; helicopter found
    (do
      (orders/set-start-date order_id (l/local-now))
      (orders/set-status order_id (:in_progress status)))
    "helicopter not found"))

(defn- complere_order [order_id end_point price]
  (orders/set-finish-date order_id (l/local-now))
  (orders/set-price order_id price)
  (orders/set-end-point order_id end_point)
  (orders/set-status order_id (:completed status)))

(defn finish_trip [order_id helicopter_id]
  (if-let [helicopter (helicopters/get-by-id helicopter_id)]
    ; helicopter found
    (if-let [order (orders/get-by-id order_id)]
      ; order found
      (let [price (calc-price (:date order) (:start_point order) (:location helicopter) (:service_class helicopter))]
        (complere_order order_id (:location helicopter) price)
        price)
      "order not found")
    "helicopter not found"))


(let [heli (helicopters/get-by-id 1)
      order_id (book_helicopter 1 {:x 1 :y 1} (l/local-now) 0)]
  (start_trip order_id 1))