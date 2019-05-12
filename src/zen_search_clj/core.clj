(ns zen-search-clj.core)

(def data
  {"users" {"_id" {"1" "Logan"
                   ""  "Pei Shi"}}})

(def prompts
  {:file  "Which file would you like to search?"
   :field "Which field should we look in?"
   :value "What's the value you'd like?"})

(def initial-state
  {:step  :file
   :file  nil
   :field nil})

(defmulti tick (fn [state _input] (:step state)))

(defmethod tick :file [state file]
  {:state (assoc state :step :field :file file)})

(defmethod tick :field [state field]
  {:state (assoc state :step :value :field field)})

(defmethod tick :value [{:keys [file field]} value]
  {:state initial-state
   :output (or (get-in data [file field value])
               "No result found.")})

(defn -main [& _args]
  (loop [state initial-state]
    (println (prompts (:step state)))

    (let [input (read-line)]
      (when-not (= input "exit")
        (let [{:keys [state output]} (tick state input)]
          (if output (println output))
          (recur state))))))
