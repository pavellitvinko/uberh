(ns helicopter_rent.views.contents
   (:use [hiccup.form]
        [hiccup.element :only (link-to)]))

(defn index []
  [:div  {:class "well"}
   [:h1 {:class "text-success"} "Hello, World"]])

(defn not-found []
  [:div {:class "well"}
   [:h1 {:class "info-warning"} "Page Not Found"]
   [:p "There's no requested page. "]
   (link-to {:class "btn btn-primary"} "/" "Go Home")])


(defn sign-form [route submit]
  [:div {:class "well"}
   (form-to  {:enctype "multipart/form-data" } 
    [:post route]
      [:div {:class "form-group"}
       (label {:class "control-label"} "email" "Email")
       (email-field {:class "form-control" :placeholder "Email"} "user-email")]
      [:div {:class "form-group"}
       (label {:class "control-label"} "password" "Password")
       (password-field {:class "form-control" :placeholder "Password"} "user-password")]
      [:div {:class "form-group"}
       (submit-button  {:class "btn"} submit)])])
     

(defn sign-in []
  (sign-form "/signin" "Sign in"))

(defn sign-up []
  (sign-form "/signup" "Sign up"))