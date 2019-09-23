package gov.nyc.doitt.casematters.submitter.domain.cmii;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import gov.nyc.doitt.casematters.submitter.domain.cmii.model.CmiiSubmission;
import gov.nyc.doitt.casematters.submitter.domain.cmii.model.CmiiSubmitterStatus;

@Repository
interface CmiiSubmissionRepository extends JpaRepository<CmiiSubmission, Integer> {

	List<CmiiSubmission> findByCmiiSubmitterStatus(CmiiSubmitterStatus cmiiSubmitterStatus);

	List<CmiiSubmission> findByCmiiSubmitterStatusIn(List<CmiiSubmitterStatus> cmiiSubmitterStatuses);

	@Lock(LockModeType.PESSIMISTIC_READ)
	List<CmiiSubmission> findByCmiiSubmitterStatusInAndSubmitterErrorCountLessThan(List<CmiiSubmitterStatus> cmiiSubmitterStatuses,
			int submitterErrorCount, Pageable pageable);

}
