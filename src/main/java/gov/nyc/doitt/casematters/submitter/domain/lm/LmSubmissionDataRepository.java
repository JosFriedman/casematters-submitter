package gov.nyc.doitt.casematters.submitter.domain.lm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gov.nyc.doitt.casematters.submitter.domain.lm.model.LmSubmissionData;
import gov.nyc.doitt.casematters.submitter.domain.lm.model.LmSubmissionDataKey;

@Repository
interface LmSubmissionDataRepository extends JpaRepository<LmSubmissionData, LmSubmissionDataKey> {

}
