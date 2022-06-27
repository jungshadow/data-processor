(ns vip.data-processor.validation.v6.spatial-boundary
  (:require [vip.data-processor.validation.v5.util :as util]
            [vip.data-processor.errors :as errors]))

(def validate-spatial-boundary
  (util/build-xml-tree-value-query-validator
   :errors :spatial-boundaries :missing :missing-external-geospatial-feature
   "SELECT xtv.path
    FROM (SELECT DISTINCT subltree(path, 0, 4) || 'ExternalGeospatialFeature' AS path
          FROM xml_tree_values WHERE results_id = ?
          AND subltree(simple_path, 0, 2) = 'VipObject.SpatialBoundary') xtv
    LEFT JOIN (SELECT path FROM xml_tree_values WHERE results_id = ?) xtv2
    ON xtv.path = subltree(xtv2.path, 0, 5)
    WHERE xtv2.path IS NULL"
   util/two-import-ids))
