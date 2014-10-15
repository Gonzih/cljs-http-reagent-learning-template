(ns cljs-playground.core
  (:require
    [figwheel.client :as fw]
    [cljs-http.client :as http]
    [cljs.core.async :refer [<! >! chan put!]]
    [reagent.core :as reagent :refer [atom]])
  (:require-macros [cljs.core.async.macros :refer [go-loop go]]))

(enable-console-print!)

(defonce users (atom []))

(defn user [user-data]
  (fn []
    [:p
     (:login user-data)]))

(defn users-list []
  [:div
   (for [user-data @users]
     ^{:key (:login user-data)} [user user-data])])

(reagent/render-component [users-list]
                          js/document.body)

(defonce request (go (let [response (<! (http/get "https://api.github.com/users" {:with-credentials? false}))]
                       (reset! users (:body response)))))

(fw/watch-and-reload; {{{
 :jsload-callback (fn []
                    ;; (stop-and-start-my app)
                    (println "RELOADED!")
                    )); }}}
