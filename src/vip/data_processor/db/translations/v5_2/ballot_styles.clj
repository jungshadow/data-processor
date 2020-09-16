(ns vip.data-processor.db.translations.v5-2.ballot-styles
  (:require [korma.core :as korma]
            [vip.data-processor.db.postgres :as postgres]
            [vip.data-processor.db.translations.util :as util]))

(defn row-fn [import-id]
  (korma/select (postgres/v5-2-tables :ballot-styles)
    (korma/where {:results_id import-id})))

(defn base-path [index]
  (str "VipObject.0.BallotStyle." index))

(defn transform-fn [idx-fn row]
  (let [path (base-path (idx-fn))
        id-path (util/id-path path)
        child-idx-fn (util/index-generator 0)]
    (conj
     (mapcat #(% child-idx-fn path row)
             [(util/simple-value->ltree :image_uri)
              (util/simple-value->ltree :ordered_contest_ids)
              (util/simple-value->ltree :party_ids)])
     {:path id-path
      :simple_path (util/path->simple-path id-path)
      :parent_with_id id-path
      :value (:id row)})))

(def transformer (util/transformer row-fn transform-fn))
