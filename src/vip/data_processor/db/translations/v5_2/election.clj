(ns vip.data-processor.db.translations.v5-2.election
  (:require [korma.core :as korma]
            [vip.data-processor.db.postgres :as postgres]
            [vip.data-processor.db.translations.util :as util]))

(defn row-fn [import-id]
  (korma/select (postgres/v5-2-tables :elections)
    (korma/where {:results_id import-id})))

(defn base-path [index]
  (str "VipObject.0.Election." index))

(defn transform-fn [idx-fn row]
  (let [path (base-path (idx-fn))
        id-path (util/id-path path)
        child-idx-fn (util/index-generator 0)]
    (conj
     (mapcat #(% child-idx-fn path row)
             [(util/internationalized-text->ltree :absentee_ballot_info)
              (util/simple-value->ltree :absentee_request_deadline)
              (util/simple-value->ltree :date)
              (util/internationalized-text->ltree :election_type)
              (util/simple-value->ltree :has_election_day_registration)
              (util/simple-value->ltree :hours_open_id)
              (util/simple-value->ltree :is_statewide)
              (util/internationalized-text->ltree :name)
              (util/internationalized-text->ltree :polling_hours)
              (util/simple-value->ltree :registration_deadline)
              (util/internationalized-text->ltree :registration_info)
              (util/simple-value->ltree :results_uri)
              (util/simple-value->ltree :state_id)])
     {:path id-path
      :simple_path (util/path->simple-path id-path)
      :parent_with_id id-path
      :value (:id row)})))

(def transformer (util/transformer row-fn transform-fn))
