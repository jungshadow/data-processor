(ns vip.data-processor.validation.v5
  (:require [vip.data-processor.validation.v5.candidate :as candidate]
            [vip.data-processor.validation.v5.email :as email]
            [vip.data-processor.validation.v5.id :as id]
            [vip.data-processor.validation.v5.precinct :as precinct]))

(def validations
  [candidate/validate-no-missing-ballot-names
   candidate/validate-pre-election-statuses
   candidate/validate-post-election-statuses
   email/validate-emails
   id/validate-unique-ids
   id/validate-no-missing-ids
   precinct/validate-no-missing-names
   precinct/validate-no-missing-locality-ids])
