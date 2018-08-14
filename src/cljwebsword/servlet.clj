(ns cljwebsword.servlet
  (:require [clojure.tools.logging :as log]
            [ring.util.servlet :refer [defservice]]
            [cljwebsword.handler :as handler]))


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
  (str *app-context* url))

(defn minus-context
  "Safely returns the uri without the context."
  [uri]
  (let [context "/cljwebsword/"]
    (if (>= (count context) (count uri))
      "/"
      (subs uri (count context)))))

(defn handle-request [request]
  (let [uri (minus-context (:uri request))]
    (let [method (:request-method request)
          query (handler/check-path uri)]
      (if (= uri "/")
        (handler/display-root)
        (if-not (nil? query)
          (handler/display-query query)
          (handler/display-404 uri))))))

(def handler
  (-> #'handle-request
      wrap-context))

(defservice handler)
