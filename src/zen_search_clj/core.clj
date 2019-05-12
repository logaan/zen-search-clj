(ns zen-search-clj.core
  (:require [cheshire.core :refer [parse-stream]]
            [clojure.java.io :refer [resource reader]]
            [clojure.pprint :refer [pprint]]))

(defn load-json [file]
  (for [entity         (-> (str file ".json") resource reader parse-stream)
        [field values] entity
        value          (flatten [values])]
    {[file field (str value)] [entity]}))

(def data
  (reduce (partial merge-with into)
          (flatten (map load-json ["users" "tickets" "organizations"]))))

(def prompts
  {:file  "Which file would you like to search?"
   :field "Which field should we look in?"
   :value "What's the value you'd like?"})

(defmulti tick (fn [state _input] (:step state)))

(defmethod tick :file [state file]
  {:state (assoc state :step :field, :file file)})

(defmethod tick :field [state field]
  {:state (assoc state :step :value, :field field)})

(defmethod tick :value [{:keys [file field]} value]
  {:state  {:step :file}
   :output (if-let [result (data [file field value])]
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
