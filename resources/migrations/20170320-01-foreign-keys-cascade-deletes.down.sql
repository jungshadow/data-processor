alter table v3_0_ballot_candidates drop constraint ballot_candidates_results_id_fkey;
alter table v3_0_ballot_line_results drop constraint ballot_line_results_results_id_fkey;
alter table v3_0_ballot_responses drop constraint ballot_responses_results_id_fkey;
alter table v3_0_ballots drop constraint ballots_results_id_fkey;
alter table v3_0_candidates drop constraint candidates_results_id_fkey;
alter table v3_0_contest_results drop constraint contest_results_results_id_fkey;
alter table v3_0_contests drop constraint contests_results_id_fkey;
alter table v3_0_custom_ballot_ballot_responses drop constraint custom_ballot_ballot_responses_results_id_fkey;
alter table v3_0_custom_ballots drop constraint custom_ballots_results_id_fkey;
alter table v3_0_early_vote_sites drop constraint early_vote_sites_results_id_fkey;
alter table v3_0_election_administrations drop constraint election_administrations_results_id_fkey;
alter table election_approvals drop constraint election_approvals_approved_result_id_fkey;
alter table v3_0_election_officials drop constraint election_officials_results_id_fkey;
alter table v3_0_elections drop constraint elections_results_id_fkey;
alter table v3_0_electoral_districts drop constraint electoral_districts_results_id_fkey;
alter table v3_0_localities drop constraint localities_results_id_fkey;
alter table v3_0_locality_early_vote_sites drop constraint locality_early_vote_sites_results_id_fkey;
alter table v5_dashboard.paths_by_locality drop constraint paths_by_locality_results_id_fkey;
alter table v3_0_polling_locations drop constraint polling_locations_results_id_fkey;
alter table v3_0_precinct_early_vote_sites drop constraint precinct_early_vote_sites_results_id_fkey;
alter table v3_0_precinct_electoral_districts drop constraint precinct_electoral_districts_results_id_fkey;
alter table v3_0_precinct_polling_locations drop constraint precinct_polling_locations_results_id_fkey;
alter table v3_0_precinct_split_electoral_districts drop constraint precinct_split_electoral_districts_results_id_fkey;
alter table v3_0_precinct_split_polling_locations drop constraint precinct_split_polling_locations_results_id_fkey;
alter table v3_0_precinct_splits drop constraint precinct_splits_results_id_fkey;
alter table v3_0_precincts drop constraint precincts_results_id_fkey;
alter table v3_0_referendum_ballot_responses drop constraint referendum_ballot_responses_results_id_fkey;
alter table v3_0_referendums drop constraint referendums_results_id_fkey;
alter table v3_0_sources drop constraint sources_results_id_fkey;
alter table v3_0_state_early_vote_sites drop constraint state_early_vote_sites_results_id_fkey;
alter table v3_0_states drop constraint states_results_id_fkey;
alter table statistics drop constraint statistics_results_id_fkey;
alter table v3_0_street_segments drop constraint street_segments_results_id_fkey;
alter table v5_1_ballot_measure_contests drop constraint v5_0_ballot_measure_contests_results_id_fkey;
alter table v5_1_ballot_selections drop constraint v5_0_ballot_selections_results_id_fkey;
alter table v5_1_ballot_styles drop constraint v5_0_ballot_styles_results_id_fkey;
alter table v5_1_candidate_contests drop constraint v5_0_candidate_contests_results_id_fkey;
alter table v5_1_candidates drop constraint v5_0_candidates_results_id_fkey;
alter table v5_1_contact_information drop constraint v5_0_contact_information_results_id_fkey;
alter table v5_1_departments drop constraint v5_0_departments_results_id_fkey;
alter table v5_1_election_administrations drop constraint v5_0_election_administrations_results_id_fkey;
alter table v5_1_elections drop constraint v5_0_elections_results_id_fkey;
alter table v5_1_electoral_districts drop constraint v5_0_electoral_districts_results_id_fkey;
alter table v5_1_localities drop constraint v5_0_localities_results_id_fkey;
alter table v5_1_offices drop constraint v5_0_offices_results_id_fkey;
alter table v5_1_parties drop constraint v5_0_parties_results_id_fkey;
alter table v5_1_people drop constraint v5_0_people_results_id_fkey;
alter table v5_1_polling_locations drop constraint v5_0_polling_locations_results_id_fkey;
alter table v5_1_precincts drop constraint v5_0_precincts_results_id_fkey;
alter table v5_1_schedules drop constraint v5_0_schedules_results_id_fkey;
alter table v5_1_sources drop constraint v5_0_sources_results_id_fkey;
alter table v5_1_states drop constraint v5_0_states_results_id_fkey;
alter table v5_1_street_segments drop constraint v5_0_street_segments_results_id_fkey;
alter table v5_1_voter_services drop constraint v5_0_voter_services_results_id_fkey;
alter table v5_1_ballot_measure_selections drop constraint v5_1_ballot_measure_selections_results_id_fkey;
alter table v5_1_candidate_selections drop constraint v5_1_candidate_selections_results_id_fkey;
alter table v5_1_contests drop constraint v5_1_contests_results_id_fkey;
alter table v5_1_ordered_contests drop constraint v5_1_ordered_contests_results_id_fkey;
alter table v5_1_party_contests drop constraint v5_1_party_contests_results_id_fkey;
alter table v5_1_party_selections drop constraint v5_1_party_selections_results_id_fkey;
alter table v5_1_retention_contests drop constraint v5_1_retention_contests_results_id_fkey;
alter table v5_statistics drop constraint v5_statistics_results_id_fkey;
alter table xml_tree_validations drop constraint xml_tree_validations_results_id_fkey;
alter table xml_tree_values drop constraint xml_tree_values_results_id_fkey;

-- Now, we can reset the constraints _without_ cascading delete
alter table v3_0_ballot_candidates add foreign key (results_id) references results(id);
alter table v3_0_ballot_line_results add foreign key (results_id) references results(id);
alter table v3_0_ballot_responses add foreign key (results_id) references results(id);
alter table v3_0_ballots add foreign key (results_id) references results(id);
alter table v3_0_candidates add foreign key (results_id) references results(id);
alter table v3_0_contest_results add foreign key (results_id) references results(id);
alter table v3_0_contests add foreign key (results_id) references results(id);
alter table v3_0_custom_ballot_ballot_responses add foreign key (results_id) references results(id);
alter table v3_0_custom_ballots add foreign key (results_id) references results(id);
alter table v3_0_early_vote_sites add foreign key (results_id) references results(id);
alter table v3_0_election_administrations add foreign key (results_id) references results(id);
alter table election_approvals add foreign key (approved_result_id) references results(id);
alter table v3_0_election_officials add foreign key (results_id) references results(id);
alter table v3_0_elections add foreign key (results_id) references results(id);
alter table v3_0_electoral_districts add foreign key (results_id) references results(id);
alter table v3_0_localities add foreign key (results_id) references results(id);
alter table v3_0_locality_early_vote_sites add foreign key (results_id) references results(id);
alter table v5_dashboard.paths_by_locality add foreign key (results_id) references results(id);
alter table v3_0_polling_locations add foreign key (results_id) references results(id);
alter table v3_0_precinct_early_vote_sites add foreign key (results_id) references results(id);
alter table v3_0_precinct_electoral_districts add foreign key (results_id) references results(id);
alter table v3_0_precinct_polling_locations add foreign key (results_id) references results(id);
alter table v3_0_precinct_split_electoral_districts add foreign key (results_id) references results(id);
alter table v3_0_precinct_split_polling_locations add foreign key (results_id) references results(id);
alter table v3_0_precinct_splits add foreign key (results_id) references results(id);
alter table v3_0_precincts add foreign key (results_id) references results(id);
alter table v3_0_referendum_ballot_responses add foreign key (results_id) references results(id);
alter table v3_0_referendums add foreign key (results_id) references results(id);
alter table v3_0_sources add foreign key (results_id) references results(id);
alter table v3_0_state_early_vote_sites add foreign key (results_id) references results(id);
alter table v3_0_states add foreign key (results_id) references results(id);
alter table statistics add foreign key (results_id) references results(id);
alter table v3_0_street_segments add foreign key (results_id) references results(id);
alter table v5_1_ballot_measure_contests add foreign key (results_id) references results(id);
alter table v5_1_ballot_selections add foreign key (results_id) references results(id);
alter table v5_1_ballot_styles add foreign key (results_id) references results(id);
alter table v5_1_candidate_contests add foreign key (results_id) references results(id);
alter table v5_1_candidates add foreign key (results_id) references results(id);
alter table v5_1_contact_information add foreign key (results_id) references results(id);
alter table v5_1_departments add foreign key (results_id) references results(id);
alter table v5_1_election_administrations add foreign key (results_id) references results(id);
alter table v5_1_elections add foreign key (results_id) references results(id);
alter table v5_1_electoral_districts add foreign key (results_id) references results(id);
alter table v5_1_localities add foreign key (results_id) references results(id);
alter table v5_1_offices add foreign key (results_id) references results(id);
alter table v5_1_parties add foreign key (results_id) references results(id);
alter table v5_1_people add foreign key (results_id) references results(id);
alter table v5_1_polling_locations add foreign key (results_id) references results(id);
alter table v5_1_precincts add foreign key (results_id) references results(id);
alter table v5_1_schedules add foreign key (results_id) references results(id);
alter table v5_1_sources add foreign key (results_id) references results(id);
alter table v5_1_states add foreign key (results_id) references results(id);
alter table v5_1_street_segments add foreign key (results_id) references results(id);
alter table v5_1_voter_services add foreign key (results_id) references results(id);
alter table v5_1_ballot_measure_selections add foreign key (results_id) references results(id);
alter table v5_1_candidate_selections add foreign key (results_id) references results(id);
alter table v5_1_contests add foreign key (results_id) references results(id);
alter table v5_1_ordered_contests add foreign key (results_id) references results(id);
alter table v5_1_party_contests add foreign key (results_id) references results(id);
alter table v5_1_party_selections add foreign key (results_id) references results(id);
alter table v5_1_retention_contests add foreign key (results_id) references results(id);
alter table v5_statistics add foreign key (results_id) references results(id);
alter table xml_tree_validations add foreign key (results_id) references results(id);
alter table xml_tree_values add foreign key (results_id) references results(id);
