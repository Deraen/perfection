(ns perfection.core
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [sablono.core :as html :refer-macros [html]]))

(enable-console-print!)

(println "Hello world 2!")

(defn add [a b]
  (+ a b))

(defcomponent widget [data owner]
  (will-mount [_]
    (om/set-state! owner :n (:init data)))
  (render-state [_ {:keys [n]}]
    (html [:div (str "Hello world: " n)
           [:ul (for [i (range 1 10)]
                  [:li [:button
                        {:on-click #(om/set-state! owner :n i)
                         :class ["btn" "btn-default" (if (= i n) "btn-success")]}
                        (str "Foobar" i)]])]])))

(om/root widget {:init 5} {:target js/document.body})
