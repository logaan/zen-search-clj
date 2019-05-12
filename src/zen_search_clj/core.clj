(ns zen-search-clj.core)

;; States:
;;
;; want-dataset -> want-field -> want-query -> want-dataset

(def greeting
  "Hello! Type exit to exit.")

(def exit
  "Goodbye!")

(defn tick [state input]
  {:state state
   :output (str "You typed: " input)})

(defn main []
  (println greeting)

  (loop [state {}]
    (let [input (read-line)]
      (when-not (= input "exit")
        (let [{:keys [state output]} (tick state input)]
          (println output)
          (recur {})))))

  (println exit))

(main)
