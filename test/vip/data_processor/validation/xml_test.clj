(ns vip.data-processor.validation.xml-test
  (:require [clojure.test :refer :all]
            [vip.data-processor.test-helpers :refer :all]
            [vip.data-processor.validation.xml :refer :all]
            [vip.data-processor.validation.data-spec :as data-spec]
            [vip.data-processor.pipeline :as pipeline]
            [vip.data-processor.db.sqlite :as sqlite]
            [korma.core :as korma]))

(deftest load-xml-test
  (testing "loads data into the db from an XML file"
    (let [ctx (merge {:input (xml-input "full-good-run.xml")
                      :data-specs data-spec/data-specs}
                     (sqlite/temp-db "full-good-run-xml"))
          out-ctx (load-xml ctx)]
      (testing "loads simple data from XML"
        (is (= [{:id 39 :name "Ohio" :election_administration_id 3456}]
               (korma/select (get-in out-ctx [:tables :states]))))
        (is (= 7
               (:cnt (first (korma/select (get-in out-ctx [:tables :ballots])
                                          (korma/aggregate (count "*") :cnt))))))
        (assert-column out-ctx :ballot-responses :text ["Yes" "No"])
        (assert-column out-ctx :contests :office ["State Treasurer"
                                                  "Attorney General"
                                                  "State Senate"
                                                  "County Commisioner"
                                                  "County Supervisor At Large"
                                                  nil
                                                  nil])
        (assert-column out-ctx :contest-results :total_votes [1002 250])
        (assert-column out-ctx :custom-ballots :heading ["Should Judge Carlton Smith be retained?"]))
      (testing "loads data from attributes"
        (assert-column out-ctx :ballot-line-results :certification (repeat 4 "certified")))
      (testing "transforms boolean values"
        (assert-column out-ctx :elections :statewide [1]))
      (testing "loads addresses"
        (is (= [{:mailing_address_line1 "P.O. Box 1776"
                 :mailing_address_city "Columbus"
                 :mailing_address_state "OH"
                 :mailing_address_zip "33333"}]
               (korma/select (get-in out-ctx [:tables :election-administrations])
                             (korma/fields :mailing_address_line1
                                           :mailing_address_city
                                           :mailing_address_state
                                           :mailing_address_zip))))
        (is (= [{:physical_address_location_name "Government Center"
                 :physical_address_line1 "12 Chad Ct."
                 :physical_address_city "Columbus"
                 :physical_address_state "OH"
                 :physical_address_zip "33333"}]
               (korma/select (get-in out-ctx [:tables :election-administrations])
                             (korma/fields :physical_address_location_name
                                           :physical_address_line1
                                           :physical_address_city
                                           :physical_address_state
                                           :physical_address_zip))))
        (is (= [{:filed_mailing_address_line1 "123 Fake St."
                 :filed_mailing_address_city "Rockville"
                 :filed_mailing_address_state "OH"
                 :filed_mailing_address_zip "20852"}]
               (korma/select (get-in out-ctx [:tables :candidates])
                             (korma/fields :filed_mailing_address_line1
                                           :filed_mailing_address_city
                                           :filed_mailing_address_state
                                           :filed_mailing_address_zip)
                             (korma/where {:id 90001}))))
        (is (= [{:non_house_address_street_direction "E"
                 :non_house_address_street_name "Guinevere"
                 :non_house_address_street_suffix "Dr"
                 :non_house_address_address_direction "SE"
                 :non_house_address_apartment "616S"
                 :non_house_address_state "VA"
                 :non_house_address_city "Annandale"
                 :non_house_address_zip "22003"}]
               (korma/select (get-in out-ctx [:tables :street-segments])
                             (korma/fields :non_house_address_street_direction
                                           :non_house_address_street_name
                                           :non_house_address_street_suffix
                                           :non_house_address_address_direction
                                           :non_house_address_apartment
                                           :non_house_address_state
                                           :non_house_address_city
                                           :non_house_address_zip)
                             (korma/where {:id 1210001}))))
        (is (= [{:address_location_name "Springfield Elementary"
                 :address_line1 "123 Main St."
                 :address_city "Fake Twp"
                 :address_state "OH"
                 :address_zip "33333"}]
               (korma/select (get-in out-ctx [:tables :polling-locations])
                             (korma/fields :address_location_name
                                           :address_line1
                                           :address_city
                                           :address_state
                                           :address_zip)
                             (korma/where {:id 20121}))))))))
