package gov.nyc.doitt.casematters.submitter.lm;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.stereotype.Component;

import gov.nyc.doitt.casematters.submitter.lm.model.LmAttachmentPath;

@Component
public class LmAttachmentPathMockerUpper {

	public List<LmAttachmentPath> createList() throws Exception {

		List<LmAttachmentPath> lmAttachmentPaths = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			LmAttachmentPath lmAttachmentPath = new LmAttachmentPath();

			FieldUtils.writeField(lmAttachmentPath, "agencyAbbreviation", "agencyAbbreviation" + i, true);
			FieldUtils.writeField(lmAttachmentPath, "attachmentPath", "attachmentPath" + i, true);
			lmAttachmentPaths.add(lmAttachmentPath);
		}
		return lmAttachmentPaths;
	}
}