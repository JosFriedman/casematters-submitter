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
	private FtpsClientWrapper ftpsClientWrapper;

	@Autowired
	private SmbConfig smbConfig;

	@Autowired
	private LmAttachmentConfig lmAttachmentConfig;

	String dir = "cm_dev_oath_fs";

	public void upload(LmSubmission lmSubmission) {

		FTPSClient ftpsClient = null;

		try {
			ftpsClient = ftpsClientWrapper.open();
			lmAttachmentConfig.setLawManagerCaseDirectory(lmSubmission);

			doUpload(lmSubmission, ftpsClient);

		} catch (Exception e) {
			String msg = "Can't upload submission: " + lmSubmission.getSubmissionID();
			logger.error(msg, e);
			throw new LmSubmitterException(msg, e);

		} finally {
			ftpsClientWrapper.close(ftpsClient);
		}
	}

	private void doUpload(LmSubmission lmSubmission, FTPSClient ftpsClient) throws MalformedURLException, SmbException {

		NtlmPasswordAuthentication smbAuth = smbConfig.getSmbAuth();
		String smbPath = "smb:" + lmSubmission.getLawManagerCaseDirectory();

		createSmbDirectory(smbAuth, smbPath);

		lmSubmission.getLmSubmissionAttachments().forEach(p -> {

			InputStream is = null;
			try {

				is = retrieveFileStream(p, ftpsClient);
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

	private InputStream retrieveFileStream(LmSubmissionAttachment lmSubmissionAttachment, FTPSClient ftpsClient)
			throws IOException {

		InputStream is = ftpsClient.retrieveFileStream(lmSubmissionAttachment.getStandardizedFileName());
		return is;
	}

	private void createSmbDirectory(NtlmPasswordAuthentication smbAuth, String smbPath) throws MalformedURLException, SmbException {

		SmbFile smbDir = new SmbFile(smbPath, smbAuth);
		if (!smbDir.exists()) {
			smbDir.mkdir();
		}
	}

	private void writeSmbFile(InputStream is, NtlmPasswordAuthentication smbAuth, String smbTarget) throws IOException {

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
