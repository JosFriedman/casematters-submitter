package gov.nyc.doitt.casematters.submitter.lm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmission;

@Repository
interface LmSubmissionRepository extends JpaRepository<LmSubmission, Integer> {

}
