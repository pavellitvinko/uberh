(ns helicopter_rent.repositories.users
  (:use [helicopter_rent.config :refer :all]
        [clojure.java.jdbc :as jdbc])
  (:gen-class))

(defn get-all []
  (jdbc/query db ["SELECT * FROM users"]))

(defn get_by_id [id]
  (jdbc/query db ["SELECT * FROM users WHERE id = ?" id]))

(defn delete [id]
  (jdbc/delete! db :user ["id = ?" id]))
 
(defn create [name email password role]
  (jdbc/insert! db :user {:name     name
                          :email    email
                          :password password
                          :role     role}))

(defn get_by_password [email password]
          (jdbc/query db ["SELECT * FROM users WHERE email = ? AND password = ?" email password]))
