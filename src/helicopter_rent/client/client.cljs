(ns helicopter_rent.client
    (:require [enfocus.core :as ef]
            [ajax.core :refer [GET POST]])
  (:require-macros [enfocus.macros :as em]))

(em/defsnippet uberh-header "/templates/uberh.html" ".uberh-header" [])
(em/defsnippet uberh-user-scope "/templates/uberh.html" "#user-scope" [])
(em/defsnippet uberh-auth-scope "/templates/uberh.html" "#auth-scope" [])
(em/defsnippet uberh-pilot-scope "/templates/uberh.html" "#pilot-scope" [])
(em/defsnippet uberh-create-order "/templates/uberh.html" "#booking-form" [])
(em/defsnippet uberh-signin-form "/templates/uberh.html" "#signin-form" [])
(em/defsnippet uberh-signup-form "/templates/uberh.html" "#signup-form" [])
(em/defsnippet uberh-pilot-create-helicopter "/templates/uberh.html" "#register-helicopter" [])
(em/defsnippet uberh-pilot-order-list "/templates/uberh.html" "#pilot-order-list" [])

;;;AUTH SCOPE
(defn set_active_signin_tab []
      (ef/at "#signin-link" (ef/add-class "active"))
      (ef/at "#signup-link" (ef/remove-class "active")))

(defn set_active_signup_tab []
      (ef/at "#signup-link" (ef/add-class "active"))
      (ef/at "#signin-link" (ef/remove-class "active")))

(defn ^:export toggle_signin []
  (ef/at "#auth-content" (ef/content (uberh-signin-form)))
  (set_active_signin_tab))

(defn ^:export toggle_signup []
  (ef/at "#auth-content" (ef/content (uberh-signup-form)))
  (set_active_signup_tab))

(defn signin-success [user-type has-helicopter]
  (ef/at ".container" (ef/content (uberh-user-scope)))
  (ef/at ".container" (ef/content (uberh-pilot-scope)))
  (ef/at "#pilot-scope" (ef/content (uberh-pilot-create-helicopter)))
  (ef/at "#pilot-scope" (ef/content (uberh-pilot-order-list))))

(defn signin-error [error-message]
  (js/alert error-message))

(defn ^:export signin []
  (.log js/console  (str {:email (ef/from "#signin-email" (ef/read-form-input))
                          :password (ef/from "#signin-password" (ef/read-form-input))}))
  (POST "/booking/signin"
        {:format  :json
         :params {:email (ef/from "#signin-email" (ef/read-form-input))
                  :password (ef/from "#signin-password" (ef/read-form-input))}
         :handler signin-success
         :error-handler signin-error}))

(defn signup-success []
  (toggle_signin))

(defn signup-error [error-message]
  (js/alert error-message))

(defn ^:export signup []
  (.log js/console  (str {:username (ef/from "#signup-username" (ef/read-form-input))
                          :email (ef/from "#signup-email" (ef/read-form-input))
                          :password (ef/from "#signup-password" (ef/read-form-input))
                          :confirm (ef/from "#signup-confirm-password" (ef/read-form-input))
                          :type (ef/from "#signup-type" (ef/read-form-input))}))
  (POST "/booking/signup"
        {:format  :json
         :params {:username (ef/from "#signup-username" (ef/read-form-input))
                  :email (ef/from "#signup-email" (ef/read-form-input))
                  :password (ef/from "#signup-password" (ef/read-form-input))
                  :confirm (ef/from "#signup-confirm-password" (ef/read-form-input))
                  :type (ef/from "#signup-type" (ef/read-form-input))}
         :handler signup-success
         :error-handler signup-error}))

;;;END AUTH SCOPE




;;;PILOT SCOPE
(defn create_helicopter_success []
      (ef/at "#pilot-scope" (ef/content (uberh-pilot-order-list))))

(defn create_helicopter_error []
    (js/alert "The helicopter didn't register. Please try again"))


(defn ^:export create_helicopter []
  (.log js/console  (str {:name (ef/from "#helicopter-name" (ef/read-form-input))
                          :description (ef/from "#helicopter-description" (ef/read-form-input))
                          :service_class (ef/from "#helicopter-service-class" (ef/read-form-input))}))
  (POST "/booking/create_helicopter"
        {:format  :json
         :params {:name (ef/from "#helicopter-name" (ef/read-form-input))
                  :description (ef/from "#helicopter-description" (ef/read-form-input))
                  :service_class (ef/from "#helicopter-service-class" (ef/read-form-input))}
         :handler create_helicopter_success
         :error-handler create_helicopter_error}))

;;;END PILOT SCOPE

(em/defsnippet uberh-edit-order "/templates/uberh.html" "#booking-form"
  [{:keys [id title body]}]
  "#booking-title" (ef/set-attr :value title)
  "#booking-body" (ef/content body)
  "#save-btn" (ef/set-attr :onclick (str "client.core.try_update_booking(" id ")")))

(em/defsnippet uberh-order-view "/templates/uberh.html" "#view-booking" [{:keys [title body]}]
  "#view-title" (ef/content title)
  "#view-body" (ef/content body))

(em/defsnippet uberh-order "/templates/uberh.html"
  ".uberh-order"  [{:keys [id title body]}]
  "#uberh-order-title" (ef/content title)
  "#uberh-order-body" (ef/content body)
  "#booking-view" (ef/set-attr :onclick
                    (str "client.core.try_view_booking(" id ")"))
  "#booking-edit" (ef/set-attr :onclick
                    (str "client.core.try_edit_booking(" id ")"))
  "#booking-delete" (ef/set-attr :onclick
  (str "if(confirm('Really delete?')) client.core.try_delete_booking(" id ")")))

(defn booking-list [data]
  (ef/at "#inner-content" (ef/content (map uberh-order data))))

(defn try-load-bookings []
  (GET "/booking/list"
      {:handler booking-list}))

(defn hide-new-order-btn []
  (ef/at "#new-order" (ef/set-attr :style "display:none;")))

(defn ^:export show-create []
  (ef/at "#inner-content" (ef/content (uberh-create-order)))
  (hide-new-order-btn))


(defn ^:export close-form []
  (start))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "Something bad happened: " status " " status-text)))

(defn booking-saved [response]
  (close-form))

(defn ^:export try-update-booking [id]
  (POST (str "/booking/update/" id)
        {:params {:title (ef/from "#booking-title" (ef/read-form-input))
                  :body  (ef/from "#booking-body" (ef/read-form-input))}
        :handler booking-saved
        :error-handler error-handler}))

(defn ^:export booking-edit [data]
  (ef/at "#inner-content" (ef/content (uberh-edit-order data)))
  (hide-new-order-btn))

(defn ^:export try-edit-booking [id]
  (GET (str "/booking/" id)
        {:handler booking-edit
        :error-handler error-handler}))

(defn booking-view [data]
  (hide-new-order-btn)
  (ef/at "#inner-content" (ef/content (uberh-order-view data))))

(defn ^:export try-view-booking [id]
  (GET (str "/booking/" id)
        {:handler booking-view
        :error-handler error-handler}))

(defn ^:export try-delete-booking [id]
  (POST (str "/booking/delete/" id)
        {:handler booking-saved
        :error-handler error-handler}))

(defn ^:export try-create-booking []
  (.log js/console  (str {:user_id (ef/from "#user_id" (ef/read-form-input))
                            :start_point_x (ef/from "#start_point_x" (ef/read-form-input))
                            :start_point_y (ef/from "#start_point_y" (ef/read-form-input))
                            :date (ef/from "#date" (ef/read-form-input))
                            :service_class (ef/from "#service_class" (ef/read-form-input))}))
  (POST "/booking/create"
        {:format  :json
        :params {:user_id (ef/from "#user_id" (ef/read-form-input))
                  :start_point_x (ef/from "#start_point_x" (ef/read-form-input))
                  :start_point_y (ef/from "#start_point_y" (ef/read-form-input))
                  :date (ef/from "#date" (ef/read-form-input))
                  :service_class (ef/from "#service_class" (ef/read-form-input))}
        :handler booking-saved
        :error-handler error-handler}))


(defmacro map-collection-to-ui [container collection method]
  (ef/at container
         (doseq [collection-item 'collection]
            (ef/append (method collection-item)))))

(defn start []
  (ef/at ".container"
         (ef/do-> (ef/content (uberh-header))
                  (ef/append (uberh-auth-scope))))
  (toggle_signin))

; (set! (.-onload js/window) start)
(set! (.-onload js/window) #(em/wait-for-load (start)))