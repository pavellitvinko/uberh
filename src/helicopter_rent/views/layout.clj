(ns helicopter_rent.views.layout
  (:use [hiccup.page :only (html5 include-css include-js)]))

(defn application [title & content]
  (html5 {:ng-app "uberh" :lang "en"}
         [:head
          [:title title]
          (include-css "//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/css/bootstrap.min.css")
          (include-css "css/styles.css")
          (include-js "scripts/script.cljs")

          [:body
           [:div {:class "container"} content ]]]))