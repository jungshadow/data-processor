CREATE TABLE ballot_line_results (id BIGINT NOT NULL,
                                  results_id BIGINT REFERENCES results (id) NOT NULL,
                                  PRIMARY KEY (results_id, id),
                                  contest_id BIGINT,
                                  jurisdiction_id BIGINT,
                                  entire_district BOOLEAN NOT NULL,
                                  candidate_id BIGINT,
                                  ballot_response_id BIGINT,
                                  votes INTEGER,
                                  overvotes INTEGER,
                                  victorious BOOLEAN,
                                  certification TEXT);
