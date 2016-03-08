(ns vip.data-processor.validation.v5
  (:require [vip.data-processor.validation.v5.candidate :as candidate]
            [vip.data-processor.validation.v5.email :as email]
            [vip.data-processor.validation.v5.external-identifiers :as external-identifiers]
            [vip.data-processor.validation.v5.id :as id]
            [vip.data-processor.validation.v5.precinct :as precinct]
            [vip.data-processor.validation.v5.retention-contest :as retention-contest]
            [vip.data-processor.validation.v5.state :as state]))

(def validations
  [candidate/validate-no-missing-ballot-names
   candidate/validate-pre-election-statuses
   candidate/validate-post-election-statuses
   email/validate-emails
   external-identifiers/validate-no-missing-types
   external-identifiers/validate-no-missing-values
   id/validate-unique-ids
   id/validate-no-missing-ids
   precinct/validate-no-missing-names
   precinct/validate-no-missing-locality-ids
   retention-contest/validate-no-missing-candidate-ids
   state/validate-no-missing-names])
