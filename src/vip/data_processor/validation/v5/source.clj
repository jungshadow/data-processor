(ns vip.data-processor.validation.v5.source
  (:require [korma.core :as korma]
            [vip.data-processor.db.postgres :as postgres]
            [vip.data-processor.validation.fips :as fips]))

(defn validate-one-source [{:keys [import-id] :as ctx}]
  (let [result (korma/exec-raw
                (:conn postgres/xml-tree-values)
                ["SELECT COUNT(DISTINCT subltree(path, 0, 4)) AS source_count
                  FROM xml_tree_values
                  WHERE results_id = ?
                  AND path ~ 'VipObject.0.Source.*'" [import-id]]
                :results)
        source-count (-> result
                         first
                         :source_count)]
    (if (> source-count 1)
      (update-in ctx [:fatal :source "VipObject.0.Source" :count]
                 conj :more-than-one)
      ctx)))

(defn validate-name [{:keys [import-id] :as ctx}]
  (let [path "VipObject.0.Source.*{1}.Name.*{1}"
        source-name (-> (korma/select postgres/xml-tree-values
                                      (korma/where {:results_id import-id})
                                      (korma/where
                                       (postgres/ltree-match
                                        postgres/xml-tree-values :path path)))
                        first
                        :value)]
    (if source-name
      ctx
      (update-in ctx [:fatal :source path :missing]
                 conj :missing-name))))

(defn validate-date-time [{:keys [import-id] :as ctx}]
  (let [path "VipObject.0.Source.*{1}.DateTime.*{1}"
        source-date-time (-> (korma/select postgres/xml-tree-values
                                           (korma/where {:results_id import-id})
                                           (korma/where
                                            (postgres/ltree-match
                                             postgres/xml-tree-values :path path)))
                             first
                             :value)]
    (if source-date-time
      ctx
      (update-in ctx [:fatal :source path :missing]
                 conj :missing-date-time))))

(defn validate-vip-id [{:keys [import-id] :as ctx}]
  (let [path "VipObject.0.Source.*{1}.VipId.*{1}"
        source-vip-id (-> (korma/select postgres/xml-tree-values
                                        (korma/where {:results_id import-id})
                                        (korma/where
                                         (postgres/ltree-match
                                          postgres/xml-tree-values :path path)))
                          first
                          :value)]
    (if source-vip-id
      ctx
      (update-in ctx [:fatal :source path :missing]
                 conj :missing-vip-id))))

(defn validate-vip-id-valid-fips [{:keys [import-id] :as ctx}]
  (let [path "VipObject.0.Source.*{1}.VipId.*{1}"
        vip-ids (korma/select postgres/xml-tree-values
                              (korma/where {:results_id import-id})
                              (korma/where
                               (postgres/ltree-match
                                postgres/xml-tree-values :path path)))
        invalid-vip-ids (remove (comp fips/valid-fips? :value) vip-ids)]
    (reduce (fn [ctx row]
              (update-in ctx
                         [:critical :source (-> row :path .getValue) :invalid-fips]
                         conj (:value row)))
            ctx invalid-vip-ids)))
