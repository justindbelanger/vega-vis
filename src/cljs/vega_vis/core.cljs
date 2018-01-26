(ns vega-vis.core
  (:require
    [clojure.pprint :as pprint]
    [reagent.core :as reagent]
    [vega-tools.core :as vt]
    [promesa.core :as p]))

(defn circle [color]
  {:width 200
   :height 200
   :marks [{:type "symbol"
            :properties {:enter {:size {:value 1000}
                                 :x {:value 100}
                                 :y {:value 100}
                                 :shape {:value "circle"}
                                 :stroke {:value color}}}}]})

(defn sales-report [year])

(def chart (atom nil))

(defn mount-chart []
  (.update (@chart {:el (.getElementById js/document "the-chart")})))

(defn render-graph [data]
  (-> data
      (vt/validate-and-parse)
      (p/catch #(reset! chart %))
      (p/then #(reset! chart %))))

(defn on-js-reload []
  (reagent/render
   [:div {:id "the-chart"}]
   (.getElementById js/document "app")))

(defn ^:export main []
  (on-js-reload))

(on-js-reload)
