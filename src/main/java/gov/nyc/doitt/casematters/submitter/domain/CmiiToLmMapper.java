package gov.nyc.doitt.casematters.submitter.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import gov.nyc.doitt.casematters.submitter.domain.cmii.CmiiSubmissionData;
import gov.nyc.doitt.casematters.submitter.domain.lm.LmSubmissionData;

/**
 * Map CmiiSubmissionData to LmSubmissionData
 * 
 */
@Component
public class CmiiToLmMapper {

	private Logger logger = LoggerFactory.getLogger(CmiiToLmMapper.class);
	
	public List<LmSubmissionData> fromCmii(List<CmiiSubmissionData> cmiiSubmissionDataList) {

		return cmiiSubmissionDataList.stream().map(p -> fromCmii(p)).collect(Collectors.toList());
	}

	public LmSubmissionData fromCmii(CmiiSubmissionData cmiiSubmissionData) {
		
		logger.debug("cmiiSubmissionData={}", cmiiSubmissionData);

		LmSubmissionData lmSubmissionData = new LmSubmissionData((int) cmiiSubmissionData.getSubmissionId(),
				cmiiSubmissionData.getEntity());
		lmSubmissionData.setMessageId((int) cmiiSubmissionData.getId());
		lmSubmissionData.setFieldValue(cmiiSubmissionData.getValue());
		
		logger.debug("lmSubmissionData={}", lmSubmissionData);

		return lmSubmissionData;
	}

}
