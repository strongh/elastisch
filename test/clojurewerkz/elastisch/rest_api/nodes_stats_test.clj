;; Copyright (c) 2011-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns clojurewerkz.elastisch.rest-api.nodes-stats-test
  (:refer-clojure :exclude [replace])
  (:require [clojurewerkz.elastisch.rest.admin :as admin]
            [clojurewerkz.elastisch.fixtures :as fx]
            [clojure.test :refer :all]))

(use-fixtures :each fx/reset-indexes fx/prepopulate-tweets-index)

(deftest ^{:rest true} nodes-stats
  (is (= #{:cluster_name :nodes}
         (into #{} (keys (admin/nodes-stats)))))
  (testing "node selection"
    (let [stats (admin/nodes-stats)
          node-id (first (keys (:nodes stats))) 
          node-name (get-in stats [:nodes node-id :name])]
      (is (empty? (:nodes (admin/nodes-stats :nodes "foo"))))
      (is (= 1 (count (:nodes (admin/nodes-stats :nodes (name node-id))))))
      (is (= 1 (count (:nodes (admin/nodes-stats :nodes (vector (name node-id)))))))
      (is (= 1 (count (:nodes (admin/nodes-stats :nodes node-name)))))))
  (testing "parameters"
    (is (not (= (admin/nodes-stats :indices true) (admin/nodes-stats :indices false))))
    (is (not (= (admin/nodes-stats :network true) (admin/nodes-stats :network false))))))
