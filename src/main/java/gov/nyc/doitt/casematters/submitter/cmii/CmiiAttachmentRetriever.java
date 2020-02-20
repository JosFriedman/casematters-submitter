package gov.nyc.doitt.casematters.submitter.cmii;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.util.TrustManagerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CmiiAttachmentRetriever {

	private Logger logger = LoggerFactory.getLogger(CmiiAttachmentRetriever.class);

	@Value("${submitter.ftp.server}")
	private String ftpServer;

	@Value("${submitter.ftp.port}")
	private int ftpPort;

	@Value("${submitter.ftp.userName}")
	private String ftpUserName;

	@Value("${submitter.ftp.password}")
	private String ftpPassword;

	@Autowired
	private CmiiAttachmentDecrypter cmiiAttachmentDecrypter;

	public File retrieveFile(FTPSClient ftpsClient, String standardizedFileName, String cmiiUniqueFileName) throws IOException {

		logger.debug("Retrieving file: {}", standardizedFileName);

		String baseFileName = String.format("%s_%s", FilenameUtils.getBaseName(standardizedFileName),
				RandomStringUtils.random(4, true, true));

		String encryptedFileName = String.format("%s", baseFileName);
		File encryptedFile = File.createTempFile(encryptedFileName, null);
		try (FileOutputStream fos = new FileOutputStream(encryptedFile)) {
			ftpsClient.retrieveFile(cmiiUniqueFileName, fos);
		}
		String decryptedFileName = cmiiAttachmentDecrypter.decrypt(encryptedFile.getPath());
		return new File(decryptedFileName);
	}

	public FTPSClient open() {

		try {
			FTPSClient ftpsClient = new FTPSClient(true);
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
			String msg = String.format("Can't open FtpsClient: server=%s, port=%d, user=%s", ftpServer, ftpPort, ftpUserName);
			logger.error(msg, e);
			throw new CmiiSubmitterException(msg, e);
		}
	}

	public void close(FTPSClient ftpsClient) {
		if (ftpsClient != null && ftpsClient.isConnected()) {
			try {
				ftpsClient.logout();
				ftpsClient.disconnect();
			} catch (IOException e) {
				logger.error("Can't logout of ftpsClient", e);
			}
		}
	}
}