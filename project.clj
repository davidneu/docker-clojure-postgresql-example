(defproject myapp "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/java.jdbc "0.7.6"]
                 [org.postgresql/postgresql "42.2.2"]]
  :main ^:skip-aot myapp.core ;; only for applications, comment out for libraries
  :target-path "target/%s" ;; only for applications, comment out for libraries
  :local-repo ".m2"
  :repl-options {:color false}
  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[clj-stacktrace "0.2.8"]
                                  [org.clojure/tools.namespace "0.2.11"]
                                  [org.clojure/java.classpath "0.3.0"]]}
             :uberjar {:aot :all}} ;; only for application, comment out for libraries
  :jvm-opts ["-Dclojure.server.repl={:port 5555 :accept cljdt/repl :address \"0.0.0.0\"}"
             "-Xms2g"
             "-Xmx2g"
             "-server"])
