(ns doitnow.test.data
  (:use clojure.test
        doitnow.data)
  (:require [clj-time.core :as time]))

(defn- uuid?
  "Tests for a UUID string: 32 characters, 0-9 & A-F, all upper-case"
  [uuid]
  (and
    (string? uuid)
    (re-matches #"[\dABCDEF]{32}" uuid)))

(deftest test-new-uuid
  (testing "Create new UUID"
    (let [uuid (new-uuid)]
      (is (uuid? uuid)))))

(deftest test-query-doits
  (testing "Query DoIts"
    (let [result (query-doits)]
      (is (seq result))
      (is (every? map? result))
      (is (every? #(contains? % :id) result))
      (is (every? #(contains? % :title) result))
      (is (every? #(contains? % :created) result)))))

(deftest test-get-doit
  (testing "Get existing DoIt"
    (let [id (key (first @doits))
          doit (get-doit id)]
      (is (map? doit))
      (is (contains? doit :title))
      (is (contains? doit :id))))
  (testing "Non-existent DoIt"
    (let [id (new-uuid)
          doit (get-doit id)]
      (is (nil? doit)))))

(deftest test-create-doit
  (testing "Create DoIt"
    (let [doit {:title "Unit Test DoIt" :description "A test DoIt"
                :due (time/plus (time/now) (time/weeks 2))}
          id (create-doit doit)]
      (is (uuid? id))
      (is (contains? @doits id)))))

(deftest test-update-doit
  (testing "Update DoIt"
    (let [id (key (first @doits))
          updated (update-doit id {:priority 1})]
      (is (not (nil? updated)))
      (is (map? updated))
      (is (contains? updated :priority)))))

(deftest test-delete-doit
  (testing "Delete DoIt"
    (let [id (key (first @doits))]
      (delete-doit id)
      (is (not (contains? @doits id))))))
