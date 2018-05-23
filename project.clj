(defproject cljwebsword "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [ring/ring-devel "1.6.3"]
                 [bidi "2.1.3"]
                 [liberator "0.15.1"]
                 [enlive "1.1.6"]]
  :main ^:skip-aot cljwebsword.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
