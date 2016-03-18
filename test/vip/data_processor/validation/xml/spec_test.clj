(ns vip.data-processor.validation.xml.spec-test
  (:require [vip.data-processor.validation.xml.spec :refer :all]
            [clojure.test :refer :all]))

(deftest type->simple-paths-test
  (testing "generates lists of simple paths for a type for a spec"
    (are [type version expected] (= (set (type->simple-paths type version)) expected)
      "Locality"     "5.0" #{"VipObject.Locality"}
      "DistrictType" "5.0" #{"VipObject.ElectoralDistrict.Type"
                             "VipObject.Locality.Type"}
      "xs:date"      "3.0" #{"vip_object.contest.filing_closed_date"
                             "vip_object.early_vote_site.end_date"
                             "vip_object.early_vote_site.start_date"
                             "vip_object.election.absentee_request_deadline"
                             "vip_object.election.date"
                             "vip_object.election.registration_deadline"}
      ;; The big one
      "InternationalizedText" "5.0" #{"VipObject.BallotMeasureContest.ConStatement"
                                      "VipObject.BallotMeasureContest.EffectOfAbstain"
                                      "VipObject.BallotMeasureContest.FullText"
                                      "VipObject.BallotMeasureContest.PassageThreshold"
                                      "VipObject.BallotMeasureContest.ProStatement"
                                      "VipObject.BallotMeasureContest.SummaryText"
                                      "VipObject.BallotMeasureSelection.Selection"
                                      "VipObject.Candidate.BallotName"
                                      "VipObject.Contest.BallotSubTitle"
                                      "VipObject.Contest.BallotTitle"
                                      "VipObject.Contest.ElectorateSpecification"
                                      "VipObject.Election.AbsenteeBallotInfo"
                                      "VipObject.Election.ElectionType"
                                      "VipObject.Election.Name"
                                      "VipObject.Election.PollingHours"
                                      "VipObject.Election.RegistrationInfo"
                                      "VipObject.ElectionAdministration.Department.ContactInformation.Hours"
                                      "VipObject.ElectionAdministration.Department.VoterService.ContactInformation.Hours"
                                      "VipObject.ElectionAdministration.Department.VoterService.Description"
                                      "VipObject.Office.ContactInformation.Hours"
                                      "VipObject.Office.Name"
                                      "VipObject.Party.Name"
                                      "VipObject.Person.ContactInformation.Hours"
                                      "VipObject.Person.FullName"
                                      "VipObject.Person.Profession"
                                      "VipObject.Person.Title"
                                      "VipObject.PollingLocation.Directions"
                                      "VipObject.PollingLocation.Hours"
                                      "VipObject.Source.Description"
                                      "VipObject.Source.FeedContactInformation.Hours"})))
