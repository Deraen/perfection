(ns user
  (:require [clojure.tools.namespace.repl :as ns-tools]
            [perfection.server :as server]))

(def system (atom nil))

(defn init []
  (swap! system (constantly {})))

(defn start []
  (swap! system server/start))

(defn stop []
  (swap! system (fn [s] (when s (server/stop s)))))

(defn go []
  (init)
  (start))

(defn reset []
  (stop)
  (ns-tools/refresh :after 'user/go))
