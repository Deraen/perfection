(ns perfection.utils
  (:require [ring.util.response :as resp]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [ring.middleware.head :refer [wrap-head]]
            [perfection.env :as env]))

;; Cache
(defn cache [req]
  (resp/header req "cache-control"
               (if (env/dev?)
                 "max=age=0,no-cache,no-store"
                 "public,max-age=86400,s-maxage=86400")))

;; Static resources
(defn- find-file [filename dirname]
  (let [f (clojure.java.io/file dirname filename)]
    (if (.isFile f)
      f)))

;; FIXME: Not used now
(defn file-resource [uri]
  (some (partial find-file (.substring uri 1)) ["./../front/.tmp" "./../front/app"]))

(defn classpath-resource [uri]
  (if-let [r (clojure.java.io/resource (str "public" uri))]
    (try
      (clojure.java.io/input-stream r)
      (catch java.io.FileNotFoundException e))))

(defn static-resource [uri]
  (if-let [resource (classpath-resource uri)]
    (-> resource
        resp/response
        cache)))

;; FIXME: Why isn't this code already a library?
(defn static-handler
  []
  (-> (fn [{:keys [uri] :as request}]
        (when-let [resource (and (not= uri "/") (static-resource uri))]
          resource))
      (wrap-file-info)
      (wrap-content-type)
      (wrap-head)))
