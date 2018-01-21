Much inspiration was taken from https://github.com/miikka/vega-tools-example for the current version of the app.

If you dont use emacs, then in a terminal do:

```clojure
    $ lein figwheel
      (... cruft ...)
    dev:cljs.user=> (in-ns 'vega-vis.core)
      nil
    dev:vega-vis.core!{:conn 2}=> (load "core")
      nil
    dev:vega-vis.core!{:conn 2}=> (swap! app-state assoc :data (:bar examples))
      (... cruft ...)
    dev:vega-vis.core!{:conn 2}=> (parse-input)
      #<Promise[~]>
```
