package gov.nyc.doitt.casematters.submitter.lm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.util.TrustManagerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FtpsClientWrapper {

	private Logger logger = LoggerFactory.getLogger(FtpsClientWrapper.class);

	@Value("${submitter.ftp.server}")
	private String ftpServer;

	@Value("${submitter.ftp.port}")
	private int ftpPort;

	@Value("${submitter.ftp.userName}")
	private String ftpUserName;

	@Value("${submitter.ftp.password}")
	private String ftpPassword;


	public FTPSClient open() throws IOException{

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
			logger.error("Can't open FtpsClient", e);
			throw e;
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