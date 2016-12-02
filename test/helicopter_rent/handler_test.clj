(ns helicopter_rent.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [helicopter_rent.handler :refer :all]))

(deftest test-app
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) "<!DOCTYPE html>\n<html><head><title>Hello World</title></head><body><h1 id=\"content\">Hello World</h1></body></html>"))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
