(defproject helicopter_rent "0.1.0-SNAPSHOT"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/java.jdbc "0.6.1"]
                 [org.clojure/clojurescript "1.9.293"]
                 [compojure "1.5.1"]
                 [mysql/mysql-connector-java "6.0.5"]
                 [ring/ring-defaults "0.2.1"]
                 [ring/ring-json "0.3.1"]
                 [ring/ring-jetty-adapter "1.3.2"]
                 [fogus/ring-edn "0.3.0"]
                 [enfocus "2.1.1"]
                 [cljs-ajax  "0.5.8"]]
  :profiles {:dev {:plugins [[lein-cljsbuild "1.1.3"]
                             [lein-ring "0.8.3"]]}}
 
  :cljsbuild {:builds [{:source-paths ["src/helicopter_rent/client"],
                        :compiler {
                          :output-to "resources/public/js/main.js"
                          :optimizations :whitespace
                          :pretty-print true
                        }}]}
  :ring {:handler helicopter_rent.handler/app
        :init helicopter_rent.handler/init
        :destroy helicopter_rent.handler/destroy
        :port 8080})