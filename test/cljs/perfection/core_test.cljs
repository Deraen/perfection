(ns perfection.core-test
  (:require [purnam.test]
            [perfection.core :as pn])
  (:require-macros [purnam.test :refer [describe it is]]))

(describe pn/add
  (it ""
    (is (pn/add 1 2) 3)))
