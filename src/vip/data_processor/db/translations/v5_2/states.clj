(ns vip.data-processor.db.translations.v5-2.states
  (:require [korma.core :as korma]
            [vip.data-processor.db.postgres :as postgres]
            [vip.data-processor.db.translations.util :as util]))

(defn row-fn [import-id]
  (korma/select (postgres/v5-2-tables :states)
    (korma/where {:results_id import-id})))

(defn base-path [index]
  (str "VipObject.0.State." index))

(defn transform-fn [idx-fn row]
  (let [path (base-path (idx-fn))
        id-path (util/id-path path)
        child-idx-fn (util/index-generator 0)]
    (conj
     (mapcat #(% child-idx-fn path row)
             [(util/simple-value->ltree :election_administration_id)
              util/external-identifiers->ltree
              (util/simple-value->ltree :name)
              (util/simple-value->ltree :polling_location_ids)])
     {:path id-path
      :simple_path (util/path->simple-path id-path)
      :parent_with_id id-path
      :value (:id row)})))

(def transformer (util/transformer row-fn transform-fn))
