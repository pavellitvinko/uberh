(ns helicopter_rent.controllers.index
  (:require [compojure.core :refer [defroutes GET POST]]
            [clojure.string :as str]
            [ring.util.response :as ring]
            [helicopter_rent.views.index :as view]
            [helicopter_rent.services.booking :as booking]))

(defn index []
  (view/index))

;(defn create
;  [shout]
;  (when-not (str/blank? shout)
;    (model/create shout))
;  (ring/redirect "/"))

(defroutes routes
           (GET "/" [] (index))
           ;(POST "/" [shout] (create shout))
           )