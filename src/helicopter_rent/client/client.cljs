(ns helicopter_rent.client
    (:require [enfocus.core :as ef]
            [ajax.core :refer [GET POST]])
  (:require-macros [enfocus.macros :as em]))

(em/defsnippet uberh-header "/templates/uberh.html" ".uberh-header" [])
(em/defsnippet uberh-content "/templates/uberh.html" "#content" [])
(em/defsnippet uberh-create-order "/templates/uberh.html" "#booking-form" [])

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

(defn start []
  (ef/at ".container"
        (ef/do-> (ef/content (uberh-header))
                  (ef/append (uberh-content))))
  (try-load-bookings))

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

; (set! (.-onload js/window) start)
(set! (.-onload js/window) #(em/wait-for-load (start)))