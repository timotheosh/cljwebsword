(ns cljwebsword.servlet
  (:require [clojure.tools.logging :as log]
            [ring.util.servlet :refer [defservice]]
            [cljwebsword.handler :as handler]
            [hiccup.core :refer [html]]
            ))

(def ^:dynamic *app-context* nil)

(defn wrap-context [handler]
  (fn [request]
    (when-let [context (:context request)]
      (log/debug (str "Request with context " context)))
    (when-let [pathdebug (:path-debug request)]
      (log/debug (str "Request with path-debug " pathdebug)))
    (when-let [servlet-context (:servlet-context request)]
      (log/debug (str "Request with servlet-context " servlet-context)))
    (when-let [servlet-context-path (:servlet-context-path request)]
      (log/debug (str "Request with servlet-context-path " servlet-context-path)))
    (binding [*app-context* (str (:context request) "/")]
      (log/debug (str "Using appcontext " *app-context*))
      (-> request
          handler))))

(defn url-in-context [url]
  (clojure.string/replace (str *app-context* url) #"[/]+" "/"))

(defn show-data [data]
  {:status 200
   :headers {}
   :body (str data)})

(defn test-request [request]
  {:status 200
   :body (pr-str request)
   :headers {}})

(defn display-404
  "Displays the given query given as an associative array."
  [query]
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body (html
          [:html
           [:head [:title "Not Found"]]
           [:body [:h1 "Sorry, Not Found"]
            [:p [:strong (str query)]]]])})

(defn handle-request [request]
  (let [data {:uri (:uri request)
              :context (:context request)
              :query-string (:query-string request)}
        uri (handler/uri-minus-context request)
        method (:request-method request)
        query (handler/check-path data)]
    (println "QUERY-DATA: " (str data))
    (println "QUERY: " (str query))
    (println "URI: " (str uri))
    (cond (= uri "/") (handler/display-root)
          query (handler/display-query query)
          :else (handler/display-404 query))))

(def handler
  (-> #'handle-request
      wrap-context))

(defservice handler)
