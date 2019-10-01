package gov.nyc.doitt.casematters.submitter.lm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.util.TrustManagerUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import gov.nyc.doitt.casematters.submitter.TestBase;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NotRealLmAttachmentUploaderTest extends TestBase {

	@Value("${submitter.ftp.server}")
	private String ftpServer;

	@Value("${submitter.ftp.port}")
	private int ftpPort;

	@Value("${submitter.ftp.userName}")
	private String ftpUserName;

	@Value("${submitter.ftp.password}")
	private String ftpPassword;

	private FTPSClient ftpsClient;

	@Before
	public void setup() throws IOException {

		ftpsClient = new FTPSClient(true);
		ftpsClient.connect(ftpServer, ftpPort);

	}

	@After
	public void teardown() throws IOException {
		ftpsClient.logout();
	}

	// @Test
	// public void givenRemoteFile_whenDownloading_thenItIsOnTheLocalFilesystem() throws IOException {
	//
	// String fullFileUrl = ftpUrl + "/cmiAttachment-ff1ae82f-4d4a-4cce-98ac-5726cd0e5dcf.dat";
	//
	// URLConnection urlConnection = new URL(fullFileUrl).openConnection();
	// InputStream inputStream = urlConnection.getInputStream();
	// Files.copy(inputStream, new File("downloaded_buz.dat").toPath());
	// inputStream.close();
	//
	// assertThat(new File("downloaded_buz.dat")).exists();
	//
	// new File("downloaded_buz.txt").delete(); // cleanup

	// }

	// @Test
	// public void ftpTest1() throws IOException {
	//
	// String remote = "/cmiAttachment-8fcf0370-6280-44b4-bbae-df3732d16613.dat";
	// String local = "ftps.dat";
	//
	// boolean connected = ftpsClient.isConnected();
	// assertTrue(connected);
	//
	// ftpsClient.setBufferSize(1000);
	// ftpsClient.setDataTimeout(1000 * 60);
	//
	// boolean loggedIn = ftpsClient.login(ftpUserName, ftpPassword);
	// assertTrue(loggedIn);
	//
	// // FTPFile[] directories = ftpsClient.listDirectories();
	// // assertNotNull(directories);
	//
	// ftpsClient.setFileType(FTP.BINARY_FILE_TYPE);
	// OutputStream output = new FileOutputStream(local);
	//
	// InputStream in = ftpsClient.retrieveFileStream(remote);
	// assertNotNull(in);
	//
	// boolean retrieved = ftpsClient.retrieveFile(remote, output);
	// assertTrue(retrieved);
	//
	// }

	@Test
	public void storeFile() throws IOException {

		String sourceFileName = "/misc/susflag.txt";
		String targetFileName = "/susflag2.txt";

		org.apache.commons.net.ftp.FTPSClient ftpClient = new org.apache.commons.net.ftp.FTPSClient(true);

		//
		// if (validateServer) {
		// ftpClient.setTrustManager(TrustManagerUtils.getValidateServerCertificateTrustManager());
		// } else {
		ftpClient.setTrustManager(TrustManagerUtils.getAcceptAllTrustManager());
		ftpClient.setRemoteVerificationEnabled(false);
		// }

		// Set agressive timeouts. The FTP server is on the local network and should
		// respond quickly, if it doesn't then something is wrong.
		ftpClient.setConnectTimeout(10 * 1000);
		ftpClient.setDataTimeout(10 * 1000);
		ftpClient.setDefaultTimeout(10 * 1000);

		// Connect to host
		ftpClient.connect(ftpServer, ftpPort);

		int reply = ftpClient.getReplyCode();

		if (FTPReply.isPositiveCompletion(reply)) {
			// Login
			if (ftpClient.login(ftpUserName, ftpPassword)) {
				ftpClient.setBufferSize(1024 * 1024);
				ftpClient.execPROT("P");
				ftpClient.execPBSZ(0);
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				ftpClient.setPassiveNatWorkaround(false);
				ftpClient.enterLocalPassiveMode();

				// Store file on host
				InputStream is = new FileInputStream(sourceFileName);

				if (!ftpClient.storeFile(targetFileName, is)) {
					System.out.println(
							"FTP transfer failed. (Source File:" + sourceFileName + ") (Target File:" + targetFileName + ")");
				}

				is.close();

				// Logout
				ftpClient.logout();

				// Disconnect
				ftpClient.disconnect();
			}
		}
	}

	@Test
	public void retrieveFile() throws IOException {

		String sourceFileName = "susflag2.txt";
		String targetFileName = "/misc/susflag.txt";

		org.apache.commons.net.ftp.FTPSClient ftpClient = new org.apache.commons.net.ftp.FTPSClient(true);

		//
		// if (validateServer) {
		// ftpClient.setTrustManager(TrustManagerUtils.getValidateServerCertificateTrustManager());
		// } else {
		ftpClient.setTrustManager(TrustManagerUtils.getAcceptAllTrustManager());
		ftpClient.setRemoteVerificationEnabled(false);
		// }

		// Set agressive timeouts. The FTP server is on the local network and should
		// respond quickly, if it doesn't then something is wrong.
		ftpClient.setConnectTimeout(10 * 1000);
		ftpClient.setDataTimeout(10 * 1000);
		ftpClient.setDefaultTimeout(10 * 1000);

		// Connect to host
		ftpClient.connect(ftpServer, ftpPort);

		int reply = ftpClient.getReplyCode();

		if (FTPReply.isPositiveCompletion(reply)) {
			// Login
			if (ftpClient.login(ftpUserName, ftpPassword)) {
				ftpClient.setBufferSize(1024 * 1024);
				ftpClient.execPROT("P");
				ftpClient.execPBSZ(0);
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				ftpClient.setPassiveNatWorkaround(false);
				ftpClient.enterLocalPassiveMode();

				// Store file on host
				OutputStream os = new FileOutputStream(targetFileName);

				if (!ftpClient.retrieveFile(sourceFileName, os)) {
					System.out.println(
							"FTP transfer failed. (Source File:" + sourceFileName + ") (Target File:" + targetFileName + ")");
				}

				os.close();

				// Logout
				ftpClient.logout();

				// Disconnect
				ftpClient.disconnect();
			}
		}
	}

	@Value("${submitter.smb.domain}")
	private String smbDomain;

	@Value("${submitter.smb.server}")
	private String smbServer;

	@Value("${submitter.smb.userName}")
	private String smbUserName;

	@Value("${submitter.smb.password}")
	private String smbPassword;

	String dir = "cm_dev_csc_fs";

	String subDir = "2019-0020";

	@Test
	public void uploadFile() throws Exception {
		

		String sourceFileName = "/misc/susflag.txt";
		String targetFileName = "susflag.txt";

		NtlmPasswordAuthentication smbAuth = new NtlmPasswordAuthentication(smbDomain, smbUserName, smbPassword);

		String smbPath = String.format("smb://%s/%s/%s/", smbServer, dir, subDir);

//		SmbFile smbDir = new SmbFile(smbPath, smbAuth);
        String smbTarget = smbPath + targetFileName;

		File sourceFile = new File(sourceFileName);
		
        SmbFile smbFile = new SmbFile(smbTarget,smbAuth);

        if (!smbFile.exists()) {
            smbFile.createNewFile();
        }
        
		SmbFileOutputStream smbOS= new SmbFileOutputStream(smbFile);

        FileUtils.copyFile(sourceFile, smbOS);

        smbOS.close();

	}

}
