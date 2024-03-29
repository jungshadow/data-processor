(ns vip.data-processor.pipelines.xml.v6
  (:require [vip.data-processor.cleanup]
            [vip.data-processor.db.postgres]
            [vip.data-processor.errors]
            [vip.data-processor.errors.process]
            [vip.data-processor.s3]
            [vip.data-processor.output.xml-helpers]
            [vip.data-processor.validation.v5.ballot-measure-contest]
            [vip.data-processor.validation.v5.candidate-contest]
            [vip.data-processor.validation.v5.candidate]
            [vip.data-processor.validation.v5.email]
            [vip.data-processor.validation.v5.external-identifiers]
            [vip.data-processor.validation.v5.id]
            [vip.data-processor.validation.v5.precinct]
            [vip.data-processor.validation.v5.source]
            [vip.data-processor.validation.v5.retention-contest]
            [vip.data-processor.validation.v5.state]
            [vip.data-processor.validation.v5.hours-open]
            [vip.data-processor.validation.v5.electoral-district]
            [vip.data-processor.validation.v5.election-administration]
            [vip.data-processor.validation.v5.election]
            [vip.data-processor.validation.v5.locality]
            [vip.data-processor.validation.v5.internationalized-text]
            [vip.data-processor.validation.v5.district-type]
            [vip.data-processor.validation.v5.office]
            [vip.data-processor.validation.v5.party]
            [vip.data-processor.validation.v5.party-selection]
            [vip.data-processor.validation.v5.ordered-contest]
            [vip.data-processor.validation.v5.street-segment]
            [vip.data-processor.validation.v5.booleans]
            [vip.data-processor.validation.v5.polling-location]
            [vip.data-processor.validation.v6.external-file]
            [vip.data-processor.validation.v6.spatial-boundary]
            [vip.data-processor.validation.xml]
            [vip.data-processor.validation.xml.v5]))

(def pipeline
  (concat
   [vip.data-processor.errors.process/process-v5-validations
    vip.data-processor.validation.xml/load-xml-ltree
    vip.data-processor.validation.xml.v5/load-xml-street-segments
    vip.data-processor.output.xml-helpers/generate-file-basename
    vip.data-processor.validation.xml/set-input-as-xml-output-file
    vip.data-processor.db.postgres/analyze-xtv
    vip.data-processor.db.postgres/store-spec-version
    vip.data-processor.db.postgres/store-public-id
    vip.data-processor.db.postgres/store-election-id
    vip.data-processor.db.postgres/populate-locality-table
    vip.data-processor.db.postgres/populate-i18n-table
    vip.data-processor.db.postgres/populate-sources-table
    vip.data-processor.db.postgres/populate-elections-table]

   ;;add validations for the types introduced with 6.0 here
   [vip.data-processor.validation.v6.external-file/validate-no-missing-file-uris
    vip.data-processor.validation.v6.spatial-boundary/validate-spatial-boundary]

   #_[vip.data-processor.validation.v5.ballot-measure-contest/validate-ballot-measure-types
    vip.data-processor.validation.v5.ballot-measure-contest/validate-no-missing-types
    vip.data-processor.validation.v5.candidate-contest/validate-no-missing-types
    vip.data-processor.validation.v5.candidate/validate-no-missing-ballot-names
    vip.data-processor.validation.v5.candidate/validate-pre-election-statuses
    vip.data-processor.validation.v5.candidate/validate-post-election-statuses
    vip.data-processor.validation.v5.email/validate-emails
    vip.data-processor.validation.v5.external-identifiers/validate-no-missing-types
    vip.data-processor.validation.v5.external-identifiers/validate-no-missing-values
    vip.data-processor.validation.v5.id/validate-unique-ids
    vip.data-processor.validation.v5.id/validate-no-missing-ids
    vip.data-processor.validation.v5.id/validate-idref-references
    vip.data-processor.validation.v5.id/validate-idrefs-references
    vip.data-processor.validation.v5.precinct/validate-no-missing-names
    vip.data-processor.validation.v5.precinct/validate-no-missing-locality-ids
    vip.data-processor.validation.v5.source/validate-one-source
    vip.data-processor.validation.v5.source/validate-name
    vip.data-processor.validation.v5.source/validate-date-time
    vip.data-processor.validation.v5.source/validate-vip-id
    vip.data-processor.validation.v5.source/validate-vip-id-valid-fips
    vip.data-processor.validation.v5.retention-contest/validate-no-missing-candidate-ids
    vip.data-processor.validation.v5.state/validate-no-missing-names
    vip.data-processor.validation.v5.hours-open/validate-times
    vip.data-processor.validation.v5.hours-open/validate-dates
    vip.data-processor.validation.v5.electoral-district/validate-no-missing-names
    vip.data-processor.validation.v5.electoral-district/validate-no-missing-types
    vip.data-processor.validation.v5.election-administration/validate-no-missing-departments
    vip.data-processor.validation.v5.election-administration/validate-voter-service-type-format
    vip.data-processor.validation.v5.election-administration/validate-no-missing-notice-texts
    vip.data-processor.validation.v5.election/validate-one-election
    vip.data-processor.validation.v5.election/validate-date
    vip.data-processor.validation.v5.election/validate-state-id
    vip.data-processor.validation.v5.locality/validate-no-missing-names
    vip.data-processor.validation.v5.locality/validate-no-missing-state-ids
    vip.data-processor.validation.v5.internationalized-text/validate-no-missing-texts
    vip.data-processor.validation.v5.district-type/validate
    vip.data-processor.validation.v5.office/validate-no-missing-names
    vip.data-processor.validation.v5.office/validate-no-missing-term-types
    vip.data-processor.validation.v5.office/validate-term-types
    vip.data-processor.validation.v5.party/validate-colors
    vip.data-processor.validation.v5.party-selection/validate-no-missing-party-ids
    vip.data-processor.validation.v5.ordered-contest/validate-no-missing-contest-ids
    vip.data-processor.validation.v5.street-segment/validate-no-missing-precinct-id
    vip.data-processor.validation.v5.street-segment/validate-no-missing-odd-even-both
    vip.data-processor.validation.v5.street-segment/validate-odd-even-both-value
    vip.data-processor.validation.v5.street-segment/validate-no-missing-city
    vip.data-processor.validation.v5.street-segment/validate-no-missing-state
    vip.data-processor.validation.v5.street-segment/validate-no-missing-zip
    vip.data-processor.validation.v5.street-segment/validate-start-house-number
    vip.data-processor.validation.v5.street-segment/validate-end-house-number
    vip.data-processor.validation.v5.street-segment/validate-no-includes-all-addresses-with-house-number-prefix
    vip.data-processor.validation.v5.street-segment/validate-no-includes-all-addresses-with-house-number-suffix
    vip.data-processor.validation.v5.street-segment/validate-no-includes-all-streets-with-house-number-prefix
    vip.data-processor.validation.v5.street-segment/validate-no-includes-all-streets-with-house-number-suffix
    vip.data-processor.validation.v5.street-segment/validate-start-end-house-number-with-house-number-prefix
    vip.data-processor.validation.v5.street-segment/validate-start-end-house-number-with-house-number-suffix
    vip.data-processor.validation.v5.street-segment/validate-no-street-segment-overlaps
    vip.data-processor.validation.v5.polling-location/validate-no-missing-address
    vip.data-processor.validation.v5.polling-location/validate-structured-address-line-1
    vip.data-processor.validation.v5.polling-location/validate-structured-address-city
    vip.data-processor.validation.v5.polling-location/validate-structured-address-state
    vip.data-processor.validation.v5.polling-location/validate-no-missing-latitudes
    vip.data-processor.validation.v5.polling-location/validate-no-missing-longitudes
    vip.data-processor.validation.v5.polling-location/validate-latitude
    vip.data-processor.validation.v5.polling-location/validate-longitude
    vip.data-processor.validation.v5.polling-location/check-for-polling-locations-mapped-to-multiple-places
    vip.data-processor.validation.v5.booleans/validate-format]

   [vip.data-processor.errors/close-errors-chan
    vip.data-processor.errors/await-statistics
    vip.data-processor.db.postgres/delete-from-xml-tree-values]))
