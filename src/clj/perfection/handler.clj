(ns perfection.handler
  (:require [ring.util.http-response :refer :all]
            [ring.util.response :refer [redirect]]
            [compojure.api.sweet :refer :all]
            [ring.swagger.schema :refer [field describe]]
            [schema.core :as s]
            [perfection.utils :refer [static-handler]]))

(defapi app
  (GET* "/" [] (redirect "index.html"))
  (static-handler)

  (swagger-ui "/api-docs")
  (swagger-docs
    :title "Api thingies"
    :description "playing with things")
  (swaggered "api"
    :description "Math with parameters"
    (context "/api" []
      (GET* "/echo" []
        :query-params [a :- String]
        (ok a)))))
