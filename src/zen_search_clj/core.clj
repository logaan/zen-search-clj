(ns zen-search-clj.core
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]))

(defn load-json [path]
  (reduce
   (partial merge-with (partial merge-with into))
   (for [entity        (json/parse-stream (io/reader (io/resource path)))
         [field value] entity]
     {field {(str value) [entity]}})))

(def data
  {"users"         (load-json "users.json")
   "tickets"       (load-json "tickets.json")
   "organizations" (load-json "organizations.json")})

(defn dump-json []
  (spit "out.json" (json/generate-string data)))

(def prompts
  {:file  "Which file would you like to search?"
   :field "Which field should we look in?"
   :value "What's the value you'd like?"})

(defmulti tick (fn [state _input] (:step state)))

(defmethod tick :file [state file]
  {:state (assoc state :step :field :file file)})

(defmethod tick :field [state field]
  {:state (assoc state :step :value :field field)})

(defmethod tick :value [{:keys [file field]} value]
  {:state {:step :file}
   :output (if-let [result (get-in data [file field value])]
             (with-out-str (pprint result))
             "No result found.")})

(defn -main [& _args]
  (loop [state {:step :file}]
    (println (prompts (:step state)))

    (let [input (read-line)]
      (when-not (= input "exit")
        (let [{:keys [state output]} (tick state input)]
          (if output (println output))
          (recur state))))))
