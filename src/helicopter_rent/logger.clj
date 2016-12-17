(ns helicopter_rent.logger
  (:import (java.io FileWriter)
           (java.util Date)))

(def logger (agent (FileWriter. "events.log" true)))

(defn- logMessage [out msg]
  (.write out (str msg "\r\n"))
  out)

(defn log
  ([message]
   (let [datetime (Date.) msg (str "[" datetime "]" message)]
     (clojure.core/send logger logMessage msg))))
