(ns perfection.core
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [sablono.core :as html :refer-macros [html]]))

(enable-console-print!)

(println "Hello world 2!")

(defonce app-state (atom {:cart {"Alla Pollo" 5}
                          :pizzas [{:name "Alla Pollo"
                                    :price    8.30
                                    :toppings {:onion 2
                                               :ham   3}}
                                   {:name "Americana"
                                    :price    7.90
                                    :toppings {:pineaple    2
                                               :ham         3
                                               :blue-cheese 1}}
                                   {:name "Margherita"
                                    :price    7.20
                                    :toppings {:mozarella 4
                                               :tomato    3}}]}))

(defcomponent pizza-item [{:keys [name price toppings]} owner]
  (init-state [_]
    {:open false})
  (render-state [_ data]
    (html [:div
           [:h3
            [:button {:on-click #(om/update-state! owner :open not)} ">"]
            (str name ", " price "€")
            [:button {:on-click (fn [] (swap! app-state update-in [:cart name] dec))} "-"]
            [:button {:on-click (fn [] (swap! app-state update-in [:cart name] inc))} "+"]]
           (if (:open data)
             [:ul (for [[topping n] toppings]
                    [:li (str topping " " n " (?)")])])])))

(defcomponent pizza-list [data owner]
  (render-state [_ _]
    (println "rendering")
    (html [:div (om/build-all pizza-item data)])))

(defcomponent shopping-cart [data owner]
  (render-state [_ _]
    (html [:div
           [:ul (for [[name qty] data]
                  [:li (str name " * " qty)])]
           [:span (str "Yhteensä: "
                       (reduce (fn [acc [name qty]]
                                 (+ acc (->> @app-state
                                             :pizzas
                                             (some #(if (= name (:name %)) %))
                                             :price
                                             (* qty))))
                               0
                               data))]])))

(defcomponent pizza-shop [data owner]
  (render-state [_ _]
    (html [:div
           [:h1 "Pizza shop"]
           (om/build pizza-list (:pizzas data))
           (om/build shopping-cart (:cart data))])))

(om/root pizza-shop app-state {:target js/document.body})
