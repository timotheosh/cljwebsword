(defproject cljwebsword "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/tools.logging "0.4.1"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-servlet "1.6.3"]
                 [ring/ring-devel "1.6.3"]
                 [org.immutant/immutant "2.1.10"]
                 [org.immutant/wildfly "2.1.10"]
                 [bidi "2.1.3"]
                 [liberator "0.15.2"]
                 [hiccup "1.0.5"]
                 [cljsword "0.2.0-SNAPSHOT"]]
  :main ^:skip-aot cljwebsword.core
  :plugins [[lein-ring "0.12.4"]
            [lein-immutant "2.1.0"]]
  :target-path "target/%s"
  :source-paths ["src"]
  :resource-paths ["resources"]
  :profiles
  {:uberjar {:omit-source true
             :aot :all
             :uberjar-name "cljwebsword.jar"}
   :dev  {:dependencies [[ring/ring-mock "0.3.2"]]}
   :test {:source-paths ["test"]}}
  :ring
  {:handler cljwebsword.handler/dev-handler}
  :immutant
  {:war
   {:resource-paths ["resources"]}})
