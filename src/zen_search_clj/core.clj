(ns zen-search-clj.core
  (:require [cheshire.core :refer [parse-stream]]
            [yaml.core :refer [generate-string]]
            [clojure.java.io :refer [resource reader]]))

(def files
  ["users" "tickets" "organizations"])

(def relationships
  [["tickets" "submitter" "users"]
   ["tickets" "assignee"  "users"]
   ["tickets" "organization" "organizations"]
   ["users"   "organization" "organizations"]])

(def prompts
  {:file  "Which file would you like to search?"
   :field "Which field should we look in?"
   :value "What's the value you'd like?"})

(defn load-json [file]
  (for [entity         (-> (str file ".json") resource reader parse-stream)
        [field values] entity
        value          (flatten [values])]
    {[file field (str value)] [entity]}))

(def data
  (reduce (partial merge-with into)
          (flatten (map load-json files))))

(defn add-related-data [file entity]
  (reduce (fn [{:strs [:_id] :as entity} [many field one]]
            (let [field-id (str field "_id")]
              (cond (= file many)
                    (assoc entity field
                           (data [one "_id" (str (entity field-id))]))

                    (= file one)
                    (assoc entity (str many "-as-" field)
                           (data [many field-id (str (entity "_id"))]))

                    :else entity)))
          entity relationships))

(defmulti tick (fn [state _input] (:step state)))

(defmethod tick :file [state file]
  {:state (assoc state :step :field, :file file)})

(defmethod tick :field [state field]
  {:state (assoc state :step :value, :field field)})

(defmethod tick :value [{:keys [file field]} value]
  {:state  {:step :file}
   :output (if-let [result (->> (data [file field value])
                                (map (partial add-related-data file)))]
             (generate-string result)
             "No result found.")})

(defn -main [& _args]
  (loop [state {:step :file}]
    (println (prompts (:step state)))

    (let [input (read-line)]
      (when-not (= input "exit")
        (let [{:keys [state output]} (tick state input)]
          (if output (println output))
          (recur state))))))

(-main)
