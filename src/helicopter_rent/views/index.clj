(ns helicopter_rent.views.index
  (:use [hiccup.page :as page])
  (:gen-class))

(defn index []
  (page/html5
    [:head
      [:title "Hello World"]]
    [:body
      [:h1 {:id "content"} "Hello World"]]))