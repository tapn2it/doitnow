(ns doitnow.test.handler
  (:use clojure.test
        ring.mock.request
        doitnow.handler))

(deftest test-api-routes
  (testing "API Options"
    (let [response (api-routes (request :options "/api"))]
      (is (= (response :status) 200))
      (is (contains? (response :body) :version))))
  (testing "API Get"
    (let [response (api-routes (request :get "/api"))]
      (is (= (response :status) 405))
      (is (nil? (response :body)))))
  (testing "Not Found"
    (let [response (api-routes (request :get "/invalid"))]
      (is (= (response :status) 404)))))