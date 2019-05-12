(ns zen-search-clj.core)

(def greeting
  "Hello! Type exit to exit.")

(def exit
  "Goodbye!")

(defn main []
  (println greeting)

  (loop [state {}]
    (let [input (read-line)]
      (when-not (= input "exit")
        (println (str "You typed: " input))
        (recur {}))))

  (println exit))

(main)

