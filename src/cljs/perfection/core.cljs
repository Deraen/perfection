(ns perfection.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [sablono.core :as html :refer-macros [html]]
            [cljs.core.async :refer [chan put! close! <!]]
            [ajax.core :refer [GET]]))

(enable-console-print!)

(defonce app-state (atom {:cart {"Alla Pollo" {:name "Alla Pollo" :qty 5 :price 8.30}}
                          :pizzas []}))

(defcomponent pizza-item [{:keys [name price toppings] :as cursor} owner]
  (init-state [_]
    {:open false})
  (render-state [_ {:keys [<cart open] :as state}]
    (html [:div
           [:h3
            [:button {:on-click #(om/update-state! owner :open not)} ">"]
            (str name ", " price "€")
            [:button {:on-click #(put! <cart {:pizza @cursor :f dec})} "-"]
            [:button {:on-click #(put! <cart {:pizza @cursor :f inc})} "+"]]
           (if open
             [:ul (for [[topping n] toppings]
                    [:li (str topping " " n " (?)")])])])))

(defcomponent pizza-list [cursor owner]
  (render-state [_ state]
    (html [:div (om/build-all pizza-item cursor {:init-state state})])))

(defcomponent shopping-cart [cursor owner]
  (render-state [_ _]
    (html [:div
           [:ul (for [[name {:keys [qty price]}] (:cart cursor)]
                  [:li (str name " * " qty)])]
           [:span (str "Yhteensä: " (reduce (fn [acc [_ {:keys [qty price]}]]
                                              (+ acc (* qty price)))
                                            0
                                            (:cart cursor)))]])))

(defn update-cart [cart {{:keys [name]} :pizza f :f}]
  (let [{:keys [qty] :as pizza} (get cart name)
        new-qty (f qty)]
    (if (> new-qty 0)
      (assoc cart name (assoc pizza :qty new-qty))
      (dissoc cart name))))

(defcomponent pizza-shop [app owner]
  (init-state [_]
    {:<cart (chan)})
  (will-mount [_]
    (GET "/api/pizzas"
      {:handler #(om/update! app [:pizzas] (clojure.walk/keywordize-keys (js->clj %)))})
    (let [<cart (om/get-state owner :<cart)]
      (go (loop []
            (let [e (<! <cart)]
              (om/transact! app :cart #(update-cart % e))
              (recur))))))
  (render-state [_ state]
    (html [:div
           [:h1 "Pizza shop"]
           (om/build pizza-list (:pizzas app) {:init-state state})
           (om/build shopping-cart {:cart (:cart app)})])))

(om/root pizza-shop app-state {:target js/document.body})
