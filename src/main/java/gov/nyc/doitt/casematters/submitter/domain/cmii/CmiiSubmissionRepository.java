package gov.nyc.doitt.casematters.submitter.domain.cmii;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CmiiSubmissionRepository extends JpaRepository<CmiiSubmission, Integer> {

	List<CmiiSubmission> findBySubmissionState(CmiiSubmissionState submissionState);
	
	List<CmiiSubmission> findBySubmissionStateIn(List<CmiiSubmissionState> submissionStates);

}
