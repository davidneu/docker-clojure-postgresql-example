(defproject myapp "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/java.jdbc "0.7.5"]
                 [org.postgresql/postgresql "9.4.1212"]]
  
  :main ^:skip-aot myapp.core ;; only for applications, comment out for libraries
  :target-path "target/%s" ;; only for applications, comment out for libraries

  :local-repo ".m2"
  
  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [;; [clj-stacktrace "0.2.8"]
                                  [org.clojure/tools.namespace "0.2.11"]
                                  [org.clojure/java.classpath "0.2.3"]]

                   :injections [;; (require 'clojure.repl)
                                (require 'complete.core)]} ;; used by inf-clojure 

             :uberjar {:aot :all}} ;; only for application, comment out for libraries
  
  :jvm-opts ["-Dclojure.server.repl={:port 5555 :accept clojure.core.server/repl :address \"0.0.0.0\"}"
             "-Xms2g"
             "-Xmx2g"
             "-server"])

