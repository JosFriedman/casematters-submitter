package gov.nyc.doitt.casematters.submitter.lm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPSClient;
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

	String dir = "cm_dev_oath_fs";

	public void upload(LmSubmission lmSubmission) {

		FTPSClient ftpsClient = null;

		try {
			cmiiAttachmentRetriever.open();
			lmAttachmentConfig.setLawManagerCaseDirectory(lmSubmission);

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
		String smbPath = "smb:" + lmSubmission.getLawManagerCaseDirectory();

		createSmbDirectory(smbAuth, smbPath);

		lmSubmission.getLmSubmissionAttachments().forEach(p -> {

			InputStream is = null;
			try {

				// is = cmiiAttachmentRetriever.retrieveFileStream(p);
				// writeSmbFile(is, smbAuth, smbPath + p.getTargetFileName());

				File cmiiFile = cmiiAttachmentRetriever.retrieveFile(p);
				writeSmbFile(cmiiFile, smbAuth, smbPath + p.getTargetFileName());

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
