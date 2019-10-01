package gov.nyc.doitt.casematters.submitter.lm;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmission;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmissionAttachment;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

@Component
public class LmAttachmentUploader {

	private Logger logger = LoggerFactory.getLogger(LmAttachmentUploader.class);

	@Autowired
	private lmStagedAttachmentRetriever ftpsClientWrapper;

	@Autowired
	private SmbConfig smbConfig;

	@Autowired
	private LmAttachmentConfig lmAttachmentConfig;

	String dir = "cm_dev_oath_fs";

	public void upload(LmSubmission lmSubmission) {

		FTPSClient ftpsClient = null;

		try {
			ftpsClientWrapper.open();
			lmAttachmentConfig.setLawManagerCaseDirectory(lmSubmission);

			doUpload(lmSubmission);

		} catch (Exception e) {
			String msg = "Can't upload submission: " + lmSubmission.getSubmissionID();
			logger.error(msg, e);
			throw new LmSubmitterException(msg, e);

		} finally {
			ftpsClientWrapper.close();
		}
	}

	private void doUpload(LmSubmission lmSubmission) throws MalformedURLException, SmbException {

		NtlmPasswordAuthentication smbAuth = smbConfig.getSmbAuth();
		String smbPath = "smb:" + lmSubmission.getLawManagerCaseDirectory();

		createSmbDirectory(smbAuth, smbPath);

		lmSubmission.getLmSubmissionAttachments().forEach(p -> {

			InputStream is = null;
			try {

				is = ftpsClientWrapper.retrieveFileStream(p);
				writeSmbFile(is, smbAuth, smbPath + p.getTargetFileName());

			} catch (Exception e) {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e2) {
					}
				}
				throw new RuntimeException(e);
			}
		});
	}
	
	private void createSmbDirectory(NtlmPasswordAuthentication smbAuth, String smbPath) throws MalformedURLException, SmbException {

		logger.debug("Creating directory: {}", smbPath);

		SmbFile smbDir = new SmbFile(smbPath, smbAuth);
		if (!smbDir.exists()) {
			smbDir.mkdir();
		}
	}

	private void writeSmbFile(InputStream is, NtlmPasswordAuthentication smbAuth, String smbTarget) throws IOException {

		logger.debug("Writing to file: {}", smbTarget);

		SmbFile smbFile = new SmbFile(smbTarget, smbAuth);
		if (!smbFile.exists()) {
			smbFile.createNewFile();
		}
		SmbFileOutputStream smbOS = new SmbFileOutputStream(smbFile);

		IOUtils.copy(is, smbOS);

		is.close();
		smbOS.close();

	}
}
