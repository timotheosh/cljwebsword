(ns cljwebsword.handler
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.reload :refer [wrap-reload]]
            [cljsword.core :as sword]
            [ring.util.codec :refer [url-encode url-decode]]))

(defn member?
  "Mimics member macro from Common Lisp. Checks if elt is in col."
  [elt col]
  (some #(= elt %) col))

(def valid-versions
  (map
   (fn [%]
     {:name (str %) :uri (str "bible/" (url-encode (.toLowerCase (str %))))})
   (sword/available-books "Biblical Texts")))

(defn query-bible
  "Queries the Bible"
  [version reference]
  (sword/readStyledText version reference 100))

(defn check-path
  [path]
  (if [(member? (first path) valid-roots)]
    (let [version (second path)
          reference (nth path 2)]
      )))

(defn handler [request]
  (let [upath (vec (remove empty? (clojure.string/split (:uri request) #"/")))
        method (:request-method request)]
    (if [(member? (first upath) valid-roots)]
      {:status 200
       :headers {"Content-Type" "text/html"}
       :body (str "<html><head><title>Request Data</title></head><body><ul>"
                  "<li>" (:uri request) "</li>"
                  "<li>" (:request-method request)  "</li>"
                  "</ul></body></html>")})))

(def dev-handler
  (wrap-reload #'handler))

(defn run-dev-server
  [port]
  (run-jetty dev-handler {:port port}))
