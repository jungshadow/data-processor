(ns vip.data-processor.validation.v6.external-file-test
  (:require  [clojure.test :refer :all]
             [vip.data-processor.pipeline :as pipeline]
             [vip.data-processor.db.postgres :as psql]
             [vip.data-processor.validation.xml :as xml]
             [vip.data-processor.test-helpers :refer :all]
             [vip.data-processor.validation.v6.external-file :as v6.external-file]
             [clojure.core.async :as a]))

(use-fixtures :once setup-postgres)

(deftest ^:postgres validate-no-missing-file-uris-success-test
  (let [errors-chan (a/chan 100)
        ctx {:errors-chan errors-chan
             :xml-source-file-path (xml-input "v6_sample_feed.xml")
             :pipeline [psql/start-run
                        xml/load-xml-ltree
                        v6.external-file/validate-no-missing-file-uris]}
        out-ctx (pipeline/run-pipeline ctx)
        errors (all-errors errors-chan)]
    (are [path]
        (assert-no-problems errors
                            {:severity :errors
                             :scope :external-file
                             :identifier path})
      "VipObject.0.ExternalFile.3.FileUri")
    #_(are [path]
        (is (contains-error? errors
                             {:severity :errors
                              :scope :candidates
                              :identifier path
                              :error-type :format}))
      "VipObject.0.Candidate.4.PreElectionStatus.0"
      "VipObject.0.Candidate.5.PreElectionStatus.0")))

(deftest ^:postgres validate-no-missing-file-uris-failure-test
  (let [errors-chan (a/chan 100)
        ctx {:errors-chan errors-chan
             :xml-source-file-path (xml-input "v6-external-file-without-uri.xml")
             :pipeline [psql/start-run
                        xml/load-xml-ltree
                        v6.external-file/validate-no-missing-file-uris]}
        out-ctx (pipeline/run-pipeline ctx)
        errors (all-errors errors-chan)]
    (testing "missing FileUris are flagged"
      (is (contains-error? errors
                           {:severity :errors
                            :scope :external-files
                            :identifier "VipObject.0.ExternalFile.0.FileUri"
                            :error-type :missing}))
      (is (contains-error? errors
                           {:severity :errors
                            :scope :external-files
                            :identifier "VipObject.0.ExternalFile.1.FileUri"
                            :error-type :missing}))
      (is (not (contains-error? errors
                                {:severity :errors
                                 :scope :external-files
                                 :identifier "VipObject.0.ExternalFile.2.FileUri"
                                 :error-type :missing}))))))
