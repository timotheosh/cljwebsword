(ns cljwebsword.routes
  (:require [bidi.ring :refer [make-handler]]
            [liberator.core :refer [defresource resource]]
            [liberator.representation :refer [ring-response]]
            [hiccup.core :refer [html]]))

(defn layout-page
  [req title message]
  (html
   [:html
    [:head
     [:title title]]
    [:body
     [:h1 title]
     [:p message]]]))

(defn not-found-handler
  [request]
  (layout-page request "Not Found" "<h1 style=\"color: red;\">Not Found</h1>"))

"Liberator library makes things easier when we just need to send the
content of arbitrary files. We just need it to send us a handler.
TODO: Write my own macro in liberator's place. Unless, of course,
there are other things I can use from the lib."
(defresource send-page [page]
  :allowed-methods [:get]
  :available-media-types ["text/html"]
  :handle-ok page
  :handle-not-found not-found-handler)

(defn index-handler
  [request]
  (layout-page request "Welcome to WebSword!" ""))

(def app-routes
  [["/"
    [["" (send-page index-handler)]]]])
