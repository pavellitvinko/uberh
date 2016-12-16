(ns helicopter_rent.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [helicopter_rent.views.contents :as contents]
            [helicopter_rent.views.layout :as layout]
            [ring.middleware.json :as middleware]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.adapter.jetty :as jetty]
            [helicopter_rent.services.booking :as booking]
            [helicopter_rent.views.contents :as contents]
            [helicopter_rent.views.layout :as layout]))
      

(defn response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json"}
   :body (pr-str data)})

(defroutes booking-routes
  (POST "/create" [user_id start_point_x start_point_y date service_class] 
        (response(booking/book_helicopter user_id {:x start_point_x :y start_point_y} date service_class))))
  ; (POST "/delete/:id" [id] (article-delete id))
  ; (POST "/update/:id" [id title body] (article-udpate id title body))
  ; (GET "/list" [] (article-list))
  ; (GET "/:id" [id] (article-view id)))

(defroutes app-routes
  (route/resources "/")
  (GET "/" [] (slurp (io/resource "public/index.html")))
  (context "/booking" [] booking-routes)
  (route/not-found (layout/application "Page Not Found" (contents/not-found)))
)

(defn init []
  (println "uberh is starting"))

(defn destroy []
  (println "uberh is shutting down"))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-params)
      (middleware/wrap-json-response)))

(defn -main [& args]
  (jetty/run-jetty app 8080))