(ns perfection.example-test
  (:require [midje.sweet :refer :all]))

(facts "foo"
  (fact "bar"
    {:a 1} => {:a 1}))
