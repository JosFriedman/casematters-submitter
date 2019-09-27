package gov.nyc.doitt.casematters.submitter.lm;

import static org.junit.Assert.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPSClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import gov.nyc.doitt.casematters.submitter.TestBase;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LmSubmitterServiceTest2 extends TestBase {

	@Value("${submitter.ftp.server}")
	private String ftpServer;

	@Value("${submitter.ftp.port}")
	private int ftpPort;

	@Value("${submitter.ftp.username}")
	private String ftpUserName;

	@Value("${submitter.ftp.password}")
	private String ftpPassword;

	private String ftpUrl;

	private FTPSClient ftpsClient;

	@Before
	public void setup() throws IOException {
		ftpUrl = String.format("ftp://%s:%s@%s:%d", ftpUserName, ftpPassword, ftpServer, ftpPort);

//		ftpsClient = new FTPSClient(true);
//		ftpsClient.connect(ftpServer, ftpPort);

	}

	@After
	public void teardown() throws IOException {
//		ftpsClient.logout();
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

	@Test
	public void ftpTest1() throws IOException {

//		String remote = "/cmiAttachment-8fcf0370-6280-44b4-bbae-df3732d16613.dat";
//		String local = "ftps.dat";
//
//		boolean connected = ftpsClient.isConnected();
//		assertTrue(connected);
//
//		ftpsClient.setBufferSize(1000);
//		ftpsClient.setDataTimeout(1000 * 60);
//
//		boolean loggedIn = ftpsClient.login(ftpUserName, ftpPassword);
//		assertTrue(loggedIn);
//
//		// FTPFile[] directories = ftpsClient.listDirectories();
//		// assertNotNull(directories);
//
//		ftpsClient.setFileType(FTP.BINARY_FILE_TYPE);
//		OutputStream output = new FileOutputStream(local);
//
//		InputStream in = ftpsClient.retrieveFileStream(remote);
//		assertNotNull(in);
//
//		boolean retrieved = ftpsClient.retrieveFile(remote, output);
//		assertTrue(retrieved);

	}

}
