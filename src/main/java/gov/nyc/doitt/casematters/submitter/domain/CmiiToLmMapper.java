package gov.nyc.doitt.casematters.submitter.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import gov.nyc.doitt.casematters.submitter.domain.cmii.CmiiSubmissionData;
import gov.nyc.doitt.casematters.submitter.domain.lm.LmSubmissionData;

/**
 * Map CmiiSubmissionData to LmSubmissionData
 * 
 */
@Component
public class CmiiToLmMapper {

	public List<LmSubmissionData> fromCmii(List<CmiiSubmissionData> cmiiSubmissionDataList) {

		return cmiiSubmissionDataList.stream().map(p -> fromCmii(p)).collect(Collectors.toList());
	}

	public LmSubmissionData fromCmii(CmiiSubmissionData cmiiSubmissionData) {

		LmSubmissionData lmSubmissionData = new LmSubmissionData((int) cmiiSubmissionData.getSubmissionId(),
				cmiiSubmissionData.getEntity());
		lmSubmissionData.setMessageId((int) cmiiSubmissionData.getId());
		lmSubmissionData.setFieldValue(cmiiSubmissionData.getValue());
		return lmSubmissionData;
	}

}
