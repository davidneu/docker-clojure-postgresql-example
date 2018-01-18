(ns myapp.core
  (:require [clojure.java.jdbc :as jdbc])  
  (:gen-class))


(def db
  {:dbtype "postgresql"
   :dbname "myapp"
   :host "db-dev"   
   :port 5432
   :user "myapp"
   :password "myapppw"
   ;; :ssl true
   ;; :sslfactory "org.postgresql.ssl.NonValidatingFactory"
   })


(defn sample-create [db n]
  (dotimes [_ n]
    (jdbc/insert! db "myapp.sample" {:x (rand)})))


(defn sample-summary [db]
  (jdbc/query
   db
   ["select count(*) as n, min(x) as min, avg(x) as mean, max(x) as max from myapp.sample"]
   {:result-set-fn first}))


(defn summary-format [{:keys [n min mean max]}]
  (format "n=%d, min=%f, mean=%f, max=%f" n min mean max))


(defn -main
  [& args]
  (let [n 100]

    (jdbc/delete! db "myapp.sample" [])
    
    (println (format "I'm going to create a sample of %d random numbers ...\n" n))
    (sample-create db n)
    
    (println "That was a lot of work, I'm going to take a nap ...\n")
    (spit "/tmp/myapp.txt" "I'm sleeping ...\n")
    (Thread/sleep (* 10 1000))

    (println "OK, now I'm going to compute some statistics on the sample ...\n")
    
    (println (summary-format (sample-summary db)))))

