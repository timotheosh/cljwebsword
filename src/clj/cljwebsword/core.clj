(ns cljwebsword.core
  (:require
   [ring.adapter.jetty :as jetty]
   [cljwebsword.handler :as handler])
  (:gen-class))

(defn -main
  "Starts the web server"
  [& args]
  (defonce server (jetty/run-jetty
                   handler/dev-handler
                   {:port 8880
                    :send-server-version? false
                    :join? false})))
