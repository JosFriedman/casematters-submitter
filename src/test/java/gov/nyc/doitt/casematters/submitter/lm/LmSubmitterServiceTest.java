package gov.nyc.doitt.casematters.submitter.lm;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Collection;

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
public class LmSubmitterServiceTest extends TestBase {

	@Value("${submitter.ftp.server}")
	private String ftpServer;

	@Value("${submitter.ftp.port}")
	private int ftpPort;

	@Value("${submitter.ftp.username}")
	private String ftpUserName;

	@Value("${submitter.ftp.password}")
	private String ftpPassword;

	private String ftpUrl;

	private FtpClient ftpClient;
	
	@Before
	public void setup() throws IOException {
		ftpUrl = String.format("ftp://%s:%s@%s:%d", ftpUserName, ftpPassword, ftpServer, ftpPort);

//		ftpClient = new FtpClient(ftpServer, ftpPort, ftpUserName, ftpPassword);
//		ftpClient.open();

	}

	@After
	public void teardown() throws IOException {
//		ftpClient.close();
		}

//	@Test
//	public void givenRemoteFile_whenDownloading_thenItIsOnTheLocalFilesystem() throws IOException {
//
//		String fullFileUrl = ftpUrl + "/cmiAttachment-ff1ae82f-4d4a-4cce-98ac-5726cd0e5dcf.dat";
//
//		URLConnection urlConnection = new URL(fullFileUrl).openConnection();
//		InputStream inputStream = urlConnection.getInputStream();
//		Files.copy(inputStream, new File("downloaded_buz.dat").toPath());
//		inputStream.close();
//
//		assertThat(new File("downloaded_buz.dat")).exists();
//
//		new File("downloaded_buz.txt").delete(); // cleanup
//	}

	@Test
	public void ftpTest1() throws IOException {

//		Collection<String> files = ftpClient.listFiles("");
//		System.out.println(files);
	}

}
