(ns cljdt
  (:require
   [clojure.core.server :as server]
   [clj-stacktrace.repl]))


(defn repl-init
  []
  (in-ns 'user)
  (apply require clojure.main/repl-requires)
  (require 'complete.core))


(defn repl []
  (clojure.main/repl
   :init repl-init
   :read server/repl-read
   :caught clj-stacktrace.repl/pst))

