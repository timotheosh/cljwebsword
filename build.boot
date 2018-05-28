(def project 'cljwebsword)
(def version "0.1.0-SNAPSHOT")

(set-env! :resource-paths #{"resources" "src/clj" "src/cljc" "src/cljs"}
          :source-paths   #{"test"}
          :dependencies   '[[org.clojure/clojure "1.8.0"]
                            [adzerk/boot-test "RELEASE" :scope "test"]
                            [ring/ring-core "1.6.3"]
                            [ring/ring-jetty-adapter "1.6.3"]
                            [ring/ring-devel "1.6.3"]
                            [hiccup "1.0.5"]
                            [ring/ring-codec "1.1.1"]
                            [org.jdom/jdom2 "2.0.4"]
                            [org.crosswire/jsword "2.1-SNAPSHOT"]])

(task-options!
 aot {:namespace   #{'cljwebsword.core}}
 pom {:project     project
      :version     version
      :description "FIXME: write description"
      :url         "http://example/FIXME"
      :scm         {:url "https://github.com/yourname/cljwebsword"}
      :license     {"Eclipse Public License"
                    "http://www.eclipse.org/legal/epl-v10.html"}}
 repl {:init-ns    'cljwebsword.core}
 jar {:main        'cljwebsword.core
      :file        (str "cljwebsword-" version "-standalone.jar")})

(deftask build
  "Build the project locally as a JAR."
  [d dir PATH #{str} "the set of directories to write to (target)."]
  (let [dir (if (seq dir) dir #{"target"})]
    (comp (aot) (pom) (uber) (jar) (target :dir dir))))

(deftask dev
  "Run server hot reloading Clojure namespaces"
  [p port PORT int "Server port (default 3000)"]
  (require '[cljwebsword.handler :as handler])
  (apply (resolve 'handler/run-dev-server) [(or port 3000)]))

(deftask run
  "Run the project."
  [a args ARG [str] "the arguments for the application."]
  (with-pass-thru fs
    (require '[cljwebsword.core :as app])
    (apply (resolve 'app/-main) args)))

(require '[adzerk.boot-test :refer [test]])
