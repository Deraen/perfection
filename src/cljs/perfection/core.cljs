(ns perfection.core
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [sablono.core :as html :refer-macros [html]]))

(enable-console-print!)

(println "Hello world 2!")

(defn add [a b]
  (+ a b))

(defcomponent widget [data owner]
  (render-state [_ _]
    (println "rendering")
    (html [:div (str "Hello world: " (:n data))
           [:ul (for [i (range 1 10)]
                  [:li [:button
                        {:on-click #(om/update! data :n i)
                         :class ["btn" "btn-default" (if (= i (:n data)) "btn-success")]}
                        (str "Foobar" i)]])]])))

(defonce app-state (atom {:n 5}))
(om/root widget app-state {:target js/document.body})
