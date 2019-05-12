(ns zen-search-clj.core-test
  (:require [clojure.test :refer :all]
            [zen-search-clj.core :refer :all]))

(deftest user-searches
  (is (= (tick {:step :value :file "users" :field "_id"} "1")
         {:state {:step :file}, :output "[{\"role\" \"admin\",\n  \"url\" \"http://initech.zendesk.com/api/v2/users/1.json\",\n  \"timezone\" \"Sri Lanka\",\n  \"shared\" false,\n  \"external_id\" \"74341f74-9c79-49d5-9611-87ef9b6eb75f\",\n  \"tags\" [\"Springville\" \"Sutton\" \"Hartsville/Hartley\" \"Diaperville\"],\n  \"email\" \"coffeyrasmussen@flotonic.com\",\n  \"name\" \"Francisca Rasmussen\",\n  \"locale\" \"en-AU\",\n  \"organization_id\" 119,\n  \"verified\" true,\n  \"signature\" \"Don't Worry Be Happy!\",\n  \"phone\" \"8335-422-718\",\n  \"_id\" 1,\n  \"created_at\" \"2016-04-15T05:19:46 -10:00\",\n  \"alias\" \"Miss Coffey\",\n  \"active\" true,\n  \"last_login_at\" \"2013-08-04T01:03:27 -10:00\",\n  \"suspended\" true}]\n"}))

  (is (= (tick {:step :value :file "users" :field "tags"} "Clinton")
         {:state {:step :file}, :output "[{\"role\" \"agent\",\n  \"url\" \"http://initech.zendesk.com/api/v2/users/71.json\",\n  \"timezone\" \"Samoa\",\n  \"shared\" false,\n  \"external_id\" \"c972bb41-94aa-4f20-bc93-e63dbfe8d9ca\",\n  \"tags\" [\"Davenport\" \"Cherokee\" \"Summertown\" \"Clinton\"],\n  \"email\" \"danahinton@flotonic.com\",\n  \"name\" \"Prince Hinton\",\n  \"locale\" \"zh-CN\",\n  \"organization_id\" 121,\n  \"verified\" false,\n  \"signature\" \"Don't Worry Be Happy!\",\n  \"phone\" \"9064-433-892\",\n  \"_id\" 71,\n  \"created_at\" \"2016-04-18T11:05:43 -10:00\",\n  \"alias\" \"Miss Dana\",\n  \"active\" true,\n  \"last_login_at\" \"2013-05-01T01:18:48 -10:00\",\n  \"suspended\" false}]\n"}))

  (is (= (count (read-string (:output (tick {:step :value :file "users" :field "active"} "true"))))
         39)))
