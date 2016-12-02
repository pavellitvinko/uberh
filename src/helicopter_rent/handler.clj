(ns helicopter_rent.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.adapter.jetty :as jetty]

            [helicopter_rent.controllers.index :as index-controller]
            [helicopter_rent.views.layout :as layout]))




;[helicopter_rent.repositories.helicopters :as helicopters]
;[helicopter_rent.repositories.orders :as orders]
;[helicopter_rent.repositories.users :as users]
;[helicopter_rent.views.index :as view-index]

(defroutes app-routes
           ; (context "/api/bookings" []
           ;   (POST "/" [user_id coordinate_from_x coordinate_from_y coordinate_to_x coordinate_to_y helicopter_type_id order_date]
           ;     (book_helicopter (booking_service.) (user_id coordinate_from_x coordinate_from_y coordinate_to_x coordinate_to_y helicopter_type_id order_date)
           ;   ))
           ; )
           index-controller/routes
           (route/resources "/")
           (route/not-found (layout/four-oh-four)))




(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-params)
      (middleware/wrap-json-response)))

(defn -main [& args]
  (jetty/run-jetty app {:port 3000}))
