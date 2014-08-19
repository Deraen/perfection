(ns perfection.server
  (:require [org.httpkit.server :refer [run-server]]))

(defn start [system]
  (require 'perfection.handler)
  (assoc system
         :http-kit (run-server (resolve 'perfection.handler/app) {:port 3000})))

(defn stop [{:keys [http-kit] :as system}]
  (when http-kit
    (http-kit))
  (dissoc system :http-kit))

(defn -main [& args]
  (start {}))

