package gov.nyc.doitt.casematters.submitter.lm;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
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
public class LmSubmitterServiceTest4 extends TestBase {

	@Value("${submitter.ftp.server}")
	private String ftpServer;

	@Value("${submitter.ftp.port}")
	private int ftpPort;

	@Value("${submitter.ftp.username}")
	private String ftpUserName;

	@Value("${submitter.ftp.password}")
	private String ftpPassword;

	private FTPClient ftpClient;

	@Before
	public void setup() throws IOException {
		
//		ftpClient = new FTPClient();
//		ftpClient.connect(ftpServer, ftpPort);

	}

	@After
	public void teardown() throws IOException {
//		ftpClient.disconnect();
	}



	@Test
	public void ftpTest1() throws IOException {

//		String remote = "cmiAttachment-8fcf0370-6280-44b4-bbae-df3732d16613.dat";
//		String local = "ftps.dat";
//
//		boolean connected = ftpClient.isConnected();
//		assertTrue(connected);
//
//		ftpClient.setBufferSize(1000);
//		ftpClient.setDataTimeout(1000 * 60);
//
//		boolean loggedIn = ftpClient.login(ftpUserName, ftpPassword);
//		assertTrue(loggedIn);
//
//		// FTPFile[] directories = ftpClient.listDirectories();
//		// assertNotNull(directories);
//
//		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
//		OutputStream output = new FileOutputStream(local);
//
//		InputStream in = ftpClient.retrieveFileStream(remote);
//		assertNotNull(in);
//
//		boolean retrieved = ftpClient.retrieveFile(remote, output);
//		assertTrue(retrieved);

	}

}
