(ns vega-vis.core
  (:require
    [clojure.pprint :as pprint]
    [reagent.core :as reagent]
    [vega-tools.core :as vt]
    [promesa.core :as p]))

;; most of this was taken from https://github.com/miikka/vega-tools-example/blob/master/src/cljs/frontend/core.cljs

(defonce examples
  {:circle
    {:width 200
     :height 200
     :marks [{:type "symbol"
              :properties {:enter {:size {:value 1000}
                                   :x {:value 100}
                                   :y {:value 100}
                                   :shape {:value "circle"}
                                   :stroke {:value "red"}}}}]}
   :bar
    {:width  400
    :height 200
    :padding {:top 10, :left 30, :bottom 30, :right 10}
    :data
    [{:name "table"
      :values [{:x 1, :y 28} {:x 2, :y 55}
               {:x 3, :y 43} {:x 4, :y 91}
               {:x 5, :y 81} {:x 6, :y 53}
               {:x 7, :y 19} {:x 8, :y 87}
               {:x 9, :y 52} {:x 10, :y 48}
               {:x 11, :y 24} {:x 12, :y 49}
               {:x 13, :y 87} {:x 14, :y 66}
               {:x 15, :y 17} {:x 16, :y 27}
               {:x 17, :y 68} {:x 18, :y 16}
               {:x 19, :y 49} {:x 20, :y 15}]}]
    :scales
    [{:name "x"
      :type "ordinal"
      :range "width"
      :domain {:data "table", :field "x"}}
     {:name "y"
      :type "linear"
      :range "height"
      :domain {:data "table", :field "y"}, :nice true}]
    :axes
    [{:type "x", :scale "x"}
     {:type "y", :scale "y"}]
    :marks
    [{:type "rect", :from {:data "table"},
      :properties {:enter {:x {:scale "x", :field "x"}
                           :width {:scale "x", :band true, :offset -1}
                           :y {:scale "y", :field "y"}
                           :y2 {:scale "y", :value 0}}
                   :update {:fill {:value "steelblue"}}
      :hover {:fill {:value "red"}}}}]}})

(defonce app-state
  (reagent/atom {}))

(defn parse-input []
  (let [{:keys [data]} @app-state]
    (swap! app-state assoc :chart nil :error nil)
    (-> data
        (vt/validate-and-parse)
        (p/catch #(swap! app-state assoc :error %))
        (p/then #(swap! app-state assoc :chart %)))))

(defn vega-chart [{:keys [chart]}]
  (reagent/create-class
    {:display-name "vega-chart"
     :reagent-render (fn [] [:div])
     :component-did-mount
        (fn [this]
          (.update (chart {:el (reagent/dom-node this)})))}))

(defn app-state-view [as]
  (let [{:keys [error chart]} @as]
    [:div
     (cond
       error [:div
              [:h2 "Validation error"]
              [:pre (with-out-str (pprint/pprint error))]]
       chart [vega-chart {:chart chart}]
       :else "Processing...")]))

(defn on-js-reload []
  (swap! app-state assoc :data (:circle examples))
  (parse-input)
  (reagent/render
    [app-state-view app-state]
    (.getElementById js/document "app")))

(defn ^:export main []
  (on-js-reload))

(on-js-reload)
