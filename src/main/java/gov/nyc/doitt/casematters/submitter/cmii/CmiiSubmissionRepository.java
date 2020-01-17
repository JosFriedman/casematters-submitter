package gov.nyc.doitt.casematters.submitter.cmii;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gov.nyc.doitt.casematters.submitter.cmii.model.CmiiSubmission;

@Repository
interface CmiiSubmissionRepository extends JpaRepository<CmiiSubmission, Integer> {

	List<CmiiSubmission> findByIdIn(List<Long> ids);

}
