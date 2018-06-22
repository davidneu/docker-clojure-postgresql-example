(ns user
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [clojure.pprint :refer (pprint)]
   [clojure.repl :refer :all]
   [clojure.test :as test]
   [clojure.tools.namespace.repl :refer (refresh refresh-all)]
   [myapp.core]))

;; (defn stop []
;;   ;; call functions in myapp.core to stop any running components
;;   )

(defn reset []
  ;; (stop)
  (clojure.tools.namespace.repl/refresh-all))

