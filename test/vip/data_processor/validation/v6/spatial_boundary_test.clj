(ns vip.data-processor.validation.v6.spatial-boundary-test
  (:require  [clojure.test :refer :all]
             [vip.data-processor.pipeline :as pipeline]
             [vip.data-processor.db.postgres :as psql]
             [vip.data-processor.validation.xml :as xml]
             [vip.data-processor.test-helpers :refer :all]
             [vip.data-processor.validation.v6.spatial-boundary :as v6.spatial-boundary]
             [clojure.core.async :as a]))

(use-fixtures :once setup-postgres)

(deftest ^:postgres validate-spatial-boundary-success-test
  (let [errors-chan (a/chan 100)
        ctx {:errors-chan errors-chan
             :xml-source-file-path (xml-input "v6_sample_feed.xml")
             :pipeline [psql/start-run
                        xml/load-xml-ltree
                        v6.spatial-boundary/validate-spatial-boundary]}
        out-ctx (pipeline/run-pipeline ctx)
        errors (all-errors errors-chan)]
    (are [path]
        (assert-no-problems errors
                            {:severity :errors
                             :scope :spatial-boundaries
                             :identifier path})
      "VipObject.0.SpatialBoundary.4.ExternalGeospatialFeature")))

(deftest ^:postgres validate-spatial-boundary-failure-test
  (let [errors-chan (a/chan 100)
        ctx {:errors-chan errors-chan
             :xml-source-file-path (xml-input "v6-spatial-boundary-without-external-geospatial-feature.xml")
             :pipeline [psql/start-run
                        xml/load-xml-ltree
                        v6.spatial-boundary/validate-spatial-boundary]}
        out-ctx (pipeline/run-pipeline ctx)
        errors (all-errors errors-chan)]
    (testing "invalid `SpatialBoundary`s are flagged"
      ;; Flag non-empty SpatialBoundary without ExternalGeospatialFeature
      (is (contains-error? errors
                           {:severity :errors
                            :scope :spatial-boundaries
                            :identifier "VipObject.0.SpatialBoundary.0.ExternalGeospatialFeature"
                            :error-type :missing}))
      ;; Don't flag SpatialBoundary with empty ExternalGeospatialFeature
      (is (not (contains-error? errors
                                {:severity :errors
                                 :scope :spatial-boundaries
                                 :identifier "VipObject.0.SpatialBoundary.1.ExternalGeospatialFeature"
                                 :error-type :missing})))
      ;; Don't flag empty SpatialBoundary without ExternalGeospatialFeature
      (is (not (contains-error? errors
                                {:severity :errors
                                 :scope :spatial-boundaries
                                 :identifier "VipObject.0.SpatialBoundary.2.ExternalGeospatialFeature"
                                 :error-type :missing})))
      ;; Don't flag SpatialBoundary valid ExternalGeospatialFeature
      (is (not (contains-error? errors
                                {:severity :errors
                                 :scope :spatial-boundaries
                                 :identifier "VipObject.0.SpatialBoundary.3.ExternalGeospatialFeature"
                                 :error-type :missing}))))))
