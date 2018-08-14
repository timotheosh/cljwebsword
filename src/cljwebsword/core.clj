(ns cljwebsword.core
  (:require
   [immutant.web :as web]
   [clojure.java.io :as io]
   [cljwebsword.servlet :as servlet]
   [cljsword.core :as cljsword])
  (:gen-class))

(defn -main
  "Starts the web server"
  [& args]
  (cljsword/set-sword-path)
  (defonce server (web/run
                    servlet/handler
                    {:port 3880
                     :auto-start true})))
