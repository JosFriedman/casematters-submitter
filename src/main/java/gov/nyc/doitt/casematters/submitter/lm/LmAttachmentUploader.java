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

	public void upload(LmSubmission lmSubmission) {

		logger.debug("Uploading lmSubmission: {}", lmSubmission);

		FTPSClient ftpsClient = null;
		try {
			ftpsClient = cmiiAttachmentRetriever.open();

			upload(ftpsClient, lmSubmission);

		} catch (Exception e) {
			String msg = String.format("Can't upload submission: %s: %s", lmSubmission.getSubmissionID(), e.getMessage());
			logger.error(msg, e);
			throw new LmSubmitterException(msg, e);

		} finally {
			cmiiAttachmentRetriever.close(ftpsClient);
		}
	}

	private void upload(FTPSClient ftpsClient, LmSubmission lmSubmission) throws MalformedURLException, SmbException {

		NtlmPasswordAuthentication smbAuth = smbConfig.getSmbAuth();
		String smbPath = createSmbDirectory(lmSubmission, smbAuth);

		lmSubmission.getLmSubmissionAttachments().forEach(p -> {
			try {
				File cmiiFile = cmiiAttachmentRetriever.retrieveFile(ftpsClient, p.getStandardizedFileName(), p.getCmiiUniqueFileName());
				writeSmbFile(cmiiFile, smbAuth, smbPath + p.getStandardizedFileName());
			} catch (Exception e) {
				throw new LmSubmitterException(e);
			}
		});
	}

	private String createSmbDirectory(LmSubmission lmSubmission, NtlmPasswordAuthentication smbAuth)
			throws MalformedURLException, SmbException {

		lmAttachmentConfig.setLawManagerCaseDirectory(lmSubmission);

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

		try (InputStream is = new FileInputStream(sourceFile); SmbFileOutputStream smbOS = new SmbFileOutputStream(smbFile)) {
			IOUtils.copy(is, smbOS);
		}
	}

}
