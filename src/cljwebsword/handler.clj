(ns cljwebsword.handler
  (:require [immutant.web :as web]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.util.codec :refer [url-encode url-decode]]
            [ring.middleware.stacktrace :refer [wrap-stacktrace]]
            [hiccup.core :refer [html]]
            [cljsword.core :as sword]))

(defn member?
  "Mimics member macro from Common Lisp. Checks if elt is in col."
  [elt col]
  (some #(= elt %) col))

(defn valid-resources
  "Return a list of valid resources based on type in lookup. The path is
  just used for how the resource will be accessed by this service."
  [path lookup]
  (map
   (fn [%]
     {:name (str %) :description (.getName %)  :uri (str path "/" (url-encode (.toLowerCase (str %))))})
   (sword/available-books lookup)))

(def valid-bibles
  (valid-resources "bible" "Biblical Texts"))

(def valid-commentaries
  (valid-resources "commentary" "Commentaries"))

(def valid-versions
  (concat
   valid-bibles
   valid-commentaries))

(defn query-bible
  "Queries the Bible"
  [version reference]
  (sword/readStyledText version reference 100))

(defn check-path
  [uri]
  (let [path (vec (remove empty? (clojure.string/split uri #"/")))
        start-path (str (first path) "/" (second path))]
    (when-not (nil? (member? start-path (vec (map #(:uri %) valid-versions))))
      (let [version (second path)]
        (if (> (count path) 2)
          {:type (nth path 0) :version version :reference (url-decode (nth path 2))}
          {:type (nth path 0) :version version :reference "Intro.Bible"})))))

(defn display-404
  "Displays the given query given as an associative array."
  [uri]
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body (html
          [:html
           [:head [:title "Not Found"]]
           [:body [:h1 "Sorry, Not Found"]
            [:p [:strong uri]]]])})

(defn display-root
  "Displays the web root of the service."
  []
  (try
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (html [:html [:head [:title "cljwebsword"]]
                  [:body [:h1 "cljwebsword"]
                   [:h2 "Bibles"]
                   [:ul
                    (map (fn [x] [:li [:a {:href (:uri x)} (:description x)]])
                         valid-bibles)]
                   [:h2 "Commentaries"]
                   [:ul
                    (map (fn [x] [:li [:a {:href (:uri x)} (:description x)]])
                         valid-commentaries)]]])}))

(defn display-query
  "Displays the given query given as an associative array."
  [query]
  (try
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body (cljsword.core/getHtml (:version query) (:reference query))}
    (catch org.crosswire.jsword.passage.NoSuchVerseException e
      {:status 510
       :headers {"Content-Type" "text/html"}
       :body (html [:html [:head [:title "Passage Not Found"]]
                    [:body [:h1 "Passage Not Found"]
                     [:p [:strong (.getMessage e)]]]])})))

(defn handler [request]
  (let [uri (:uri request)]
    (let [method (:request-method request)
          query (check-path uri)]
      (if (= uri "/")
        (display-root)
        (if-not (nil? query)
          (display-query query)
          (display-404 uri))))))

(def dev-handler
  (-> #'handler
      wrap-stacktrace
      wrap-reload))

(defn run-dev-server
  [port]
  (web/run dev-handler {:port port}))