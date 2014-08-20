(defproject perfection "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 ;; backend
                 [http-kit "2.1.16"]
                 [metosin/compojure-api "0.15.1"]
                 [metosin/ring-http-response "0.4.1"]
                 [metosin/ring-swagger-ui "2.0.17"]
                 [metosin/clojure-bootcamp.dataset "0.1.0"]

                 ;; cljs
                 [org.clojure/clojurescript "0.0-2311"]
                 [im.chit/purnam.test "0.4.3"]
                 [om "0.7.1"]
                 [prismatic/om-tools "0.3.2" :exclusions [org.clojure/clojure]]
                 [sablono "0.2.21"]
                 [figwheel "0.1.4-SNAPSHOT"]]

  :source-paths ["src/clj"]
  :test-paths ["test/clj"]

  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.5"]
                                  [midje "1.6.3"]]
                   :source-paths ["dev-src"]}}

  :main perfection.server
  :repl-options {:init-ns user}

  :jvm-opts ["-Xmx1G"]

  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-figwheel "0.1.4-SNAPSHOT"]
            [com.cemerick/austin "0.1.4"]
            [lein-midje "3.1.3"]]

  :figwheel {
    :http-server-root "public"
    :port 3449
    :css-dirs ["resources/public/css"]}

  :cljsbuild {
    :builds [{:id "dev"
              :source-paths ["src/cljs" "src/figwheel" "src/brepl"]
              :compiler {
                :output-to "resources/public/perfection.js"
                :output-dir "resources/public/out"
                :optimizations :none
                :source-map true}}
             {:id "release"
              :source-paths ["src/cljs"]
              :compiler {
                :output-to "resources/public/perfection_prod.js"
                :output-dir "resources/public/prod-out"
                :optimizations :advanced
                :pretty-print false
                :source-map "resources/public/perfection_prod.js.map"
                :preamble ["react/react.min.js"]
                :externs ["react/externs/react.js"]}}
             {:id "test"
              :source-paths ["src/cljs" "test/cljs"]
              :compiler {
                :output-to "target/perfection_test.js"
                :optimizations :whitespace
                :preamble ["react/react.min.js"]
                :externs ["react/externs/react.js"]}}]})
