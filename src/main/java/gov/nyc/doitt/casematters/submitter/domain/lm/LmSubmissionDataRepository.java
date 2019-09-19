package gov.nyc.doitt.casematters.submitter.domain.lm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface LmSubmissionDataRepository extends JpaRepository<LmSubmissionData, LmSubmissionDataKey> {

}
