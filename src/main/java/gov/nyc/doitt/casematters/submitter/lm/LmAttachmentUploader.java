package gov.nyc.doitt.casematters.submitter.lm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.nyc.doitt.casematters.submitter.cmii.CmiiAttachmentRetriever;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmission;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

@Component
public class LmAttachmentUploader {

	private Logger logger = LoggerFactory.getLogger(LmAttachmentUploader.class);

	@Autowired
	private CmiiAttachmentRetriever cmiiAttachmentRetriever;

	@Autowired
	private SmbConfig smbConfig;

	@Autowired
	private LmAttachmentConfig lmAttachmentConfig;

	public void upload(LmSubmission lmSubmission) {

		logger.debug("Uploading lmSubmission: {}", lmSubmission);

		try {
			lmAttachmentConfig.setLawManagerCaseDirectory(lmSubmission);
			cmiiAttachmentRetriever.open();

			doUpload(lmSubmission);

		} catch (Exception e) {
			String msg = "Can't upload submission: " + lmSubmission.getSubmissionID();
			logger.error(msg, e);
			throw new LmSubmitterException(msg, e);

		} finally {
			cmiiAttachmentRetriever.close();
		}
	}

	private void doUpload(LmSubmission lmSubmission) throws MalformedURLException, SmbException {

		NtlmPasswordAuthentication smbAuth = smbConfig.getSmbAuth();
		String smbPath = createSmbDirectory(lmSubmission, smbAuth);

		lmSubmission.getLmSubmissionAttachments().forEach(p -> {
			try {
				File cmiiFile = cmiiAttachmentRetriever.retrieveFile(p);
				writeSmbFile(cmiiFile, smbAuth, smbPath + p.getStandardizedFileName());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

	private String createSmbDirectory(LmSubmission lmSubmission, NtlmPasswordAuthentication smbAuth)
			throws MalformedURLException, SmbException {

		String smbPath = String.format("smb:%s/", lmSubmission.getLawManagerCaseDirectory().replace("\\", "/"));
		logger.debug("Creating directory (if it does not already exist): {}", smbPath);

		SmbFile smbDir = new SmbFile(smbPath, smbAuth);
		if (!smbDir.exists()) {
			smbDir.mkdir();
		}
		return smbPath;
	}

	private void writeSmbFile(File sourceFile, NtlmPasswordAuthentication smbAuth, String smbTargetFileName) throws IOException {

		logger.debug("Writing source file {} to Smb file: {}", sourceFile.getPath(), smbTargetFileName);

		SmbFile smbFile = new SmbFile(smbTargetFileName, smbAuth);
		if (!smbFile.exists()) {
			smbFile.createNewFile();
		}

		InputStream is = new FileInputStream(sourceFile);
		SmbFileOutputStream smbOS = new SmbFileOutputStream(smbFile);
		IOUtils.copy(is, smbOS);
		is.close();
		smbOS.close();
	}

}
