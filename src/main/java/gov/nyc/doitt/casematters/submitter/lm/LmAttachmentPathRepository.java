package gov.nyc.doitt.casematters.submitter.lm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gov.nyc.doitt.casematters.submitter.lm.model.LmAttachmentPath;

@Repository
interface LmAttachmentPathRepository extends JpaRepository<LmAttachmentPath, String> {
}
