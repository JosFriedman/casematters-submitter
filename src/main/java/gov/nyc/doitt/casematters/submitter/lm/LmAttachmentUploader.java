package gov.nyc.doitt.casematters.submitter.lm;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.util.TrustManagerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmission;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmissionAttachment;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

@Component
public class LmAttachmentUploader {

	// \\msdwvw-ctwcmwb1.csc.nycnet\CM_DEV_OATH_FS

	private Logger logger = LoggerFactory.getLogger(LmAttachmentUploader.class);

	@Value("${submitter.ftp.server}")
	private String ftpServer;

	@Value("${submitter.ftp.port}")
	private int ftpPort;

	@Value("${submitter.ftp.userName}")
	private String ftpUserName;

	@Value("${submitter.ftp.password}")
	private String ftpPassword;

	public void upload(LmSubmission lmSubmission) {

		FTPSClient ftpsClient = getFtpsClient();

		try {
			String subDir = "" + lmSubmission.getSubmissionID();
			String smbPath = String.format("//%s/%s/%s/", smbServer, dir, subDir);

			lmSubmission.setLawManagerCaseDirectory(smbPath);
			doUpload(lmSubmission, ftpsClient);

		} catch (Exception e) {
			logger.error("Can't upload submission: " + lmSubmission.getSubmissionID(), e);

		} finally {
			if (ftpsClient != null && ftpsClient.isConnected()) {
				try {
					ftpsClient.logout();
					ftpsClient.disconnect();
				} catch (IOException e) {
					logger.error("Can't logout of ftpsClient for submission: " + lmSubmission.getSubmissionID(), e);
					throw new RuntimeException(e);
				}
			}
		}
	}

	private FTPSClient getFtpsClient() {

		FTPSClient ftpsClient = null;
		try {
			ftpsClient = new FTPSClient(true);
			ftpsClient.connect(ftpServer, ftpPort);

			ftpsClient.setTrustManager(TrustManagerUtils.getAcceptAllTrustManager());
			ftpsClient.setRemoteVerificationEnabled(false);

			// Set aggressive timeouts. The FTP server is on the local network and should
			// respond quickly, if it doesn't then something is wrong.
			ftpsClient.setConnectTimeout(10 * 1000);
			ftpsClient.setDataTimeout(10 * 1000);
			ftpsClient.setDefaultTimeout(10 * 1000);

			// Connect to host
			ftpsClient.connect(ftpServer, ftpPort);

			int reply = ftpsClient.getReplyCode();

			if (FTPReply.isPositiveCompletion(reply)) {
				// Login
				if (ftpsClient.login(ftpUserName, ftpPassword)) {
					ftpsClient.setBufferSize(1024 * 1024);
					ftpsClient.execPROT("P");
					ftpsClient.execPBSZ(0);
					ftpsClient.setFileType(FTP.BINARY_FILE_TYPE);
					ftpsClient.setPassiveNatWorkaround(false);
					ftpsClient.enterLocalPassiveMode();
				}
			}

			return ftpsClient;

		} catch (IOException e) {
			logger.error("Can't getFtpsClient", e);
			throw new RuntimeException(e);
		}
	}

	private void doUpload(LmSubmission lmSubmission, FTPSClient ftpsClient) {

		lmSubmission.getLmSubmissionAttachments().forEach(p -> {

			InputStream is;
			try {
				is = retrieveFileStream(p, ftpsClient);
				storeOnSmb(lmSubmission.getLawManagerCaseDirectory(), p, is);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

	private InputStream retrieveFileStream(LmSubmissionAttachment lmSubmissionAttachment, FTPSClient ftpsClient)
			throws IOException {

		InputStream is = ftpsClient.retrieveFileStream(lmSubmissionAttachment.getStandardizedFileName());
		return is;
	}

	@Value("${submitter.smb.domain}")
	private String smbDomain;

	@Value("${submitter.smb.server}")
	private String smbServer;

	@Value("${submitter.smb.userName}")
	private String smbUserName;

	@Value("${submitter.smb.password}")
	private String smbPassword;

	String dir = "cm_dev_oath_fs";

	private void storeOnSmb(String lawManagerCaseDirectory, LmSubmissionAttachment lmSubmissionAttachment, InputStream is)
			throws Exception {

		NtlmPasswordAuthentication smbAuth = new NtlmPasswordAuthentication(smbDomain, smbUserName, smbPassword);

		String smbPath = "smb:" + lawManagerCaseDirectory;
		SmbFile smbDir = new SmbFile(smbPath, smbAuth);

		if (!smbDir.exists()) {
			smbDir.mkdir();
		}
		String targetFileName = FilenameUtils.getBaseName(lmSubmissionAttachment.getStandardizedFileName()) + "."
				+ lmSubmissionAttachment.getExtension();

		String smbTarget = smbPath + targetFileName;

		SmbFile smbFile = new SmbFile(smbTarget, smbAuth);
		if (!smbFile.exists()) {
			smbFile.createNewFile();
		}
		SmbFileOutputStream smbOS = new SmbFileOutputStream(smbFile);

		IOUtils.copy(is, smbOS);

		smbOS.close();

	}
}
