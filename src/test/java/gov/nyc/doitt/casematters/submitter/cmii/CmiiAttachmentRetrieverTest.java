package gov.nyc.doitt.casematters.submitter.cmii;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.UnknownHostException;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import gov.nyc.doitt.casematters.submitter.TestBase;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmission;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmissionAttachment;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmissionMockerUpper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CmiiAttachmentRetrieverTest extends TestBase {

	private FakeFtpServer fakeFtpServer;

	@Value("${submitter.ftp.server}")
	private String ftpServer;

	@Value("${submitter.ftp.port}")
	private int ftpPort;

	@Value("${submitter.ftp.userName}")
	private String ftpUserName;

	@Value("${submitter.ftp.password}")
	private String ftpPassword;

	@Mock
	private CmiiAttachmentDecrypter cmiiAttachmentDecrypter;

	@InjectMocks
	private CmiiAttachmentRetriever cmiiAttachmentRetriever = new CmiiAttachmentRetriever();

	@Autowired
	private LmSubmissionMockerUpper lmSubmissionMockerUpper;

	@Before
	public void setup() throws Exception {

		FieldUtils.writeField(cmiiAttachmentRetriever, "ftpServer", ftpServer, true);
		FieldUtils.writeField(cmiiAttachmentRetriever, "ftpPort", ftpPort, true);
		FieldUtils.writeField(cmiiAttachmentRetriever, "ftpUserName", ftpUserName, true);
		FieldUtils.writeField(cmiiAttachmentRetriever, "ftpPassword", ftpPassword, true);

		fakeFtpServer = new FakeFtpServer();
		fakeFtpServer.addUserAccount(new UserAccount(ftpUserName, ftpPassword, ""));
		FileSystem fileSystem = new UnixFakeFileSystem();
		fakeFtpServer.setFileSystem(fileSystem);
		fakeFtpServer.setServerControlPort(0);
		fakeFtpServer.start();
	}

	@After
	public void teardown() throws Exception {

		fakeFtpServer.stop();
	}

	@Test
	public void testOpenClose() throws Exception {

		FTPSClient ftpsClient = cmiiAttachmentRetriever.open();
		assertTrue(FTPReply.isPositiveCompletion(ftpsClient.getReplyCode()));

		cmiiAttachmentRetriever.close(ftpsClient);
	}

	@Test
	public void testOpen_Fail() throws Exception {

		FieldUtils.writeField(cmiiAttachmentRetriever, "ftpServer", "bad", true);

		try {
			cmiiAttachmentRetriever.open();
			assertTrue(false);
		} catch (CmiiSubmitterException e) {
			assertEquals(UnknownHostException.class, e.getCause().getClass());
		}
	}

	@Test
	public void testRetrieveFile() throws Exception {

		LmSubmission lmSubmission = lmSubmissionMockerUpper.create();
		LmSubmissionAttachment lmSubmissionAttachment = lmSubmission.getLmSubmissionAttachments().get(0);

		FTPSClient ftpsClient = cmiiAttachmentRetriever.open();

		when(cmiiAttachmentDecrypter.decrypt(anyString())).thenReturn("foo.txt");

		File file = cmiiAttachmentRetriever.retrieveFile(ftpsClient, lmSubmissionAttachment.getStandardizedFileName(),
				lmSubmissionAttachment.getCmiiUniqueFileName());

		assertNotNull(file);
		assertEquals("foo.txt", file.getPath());
	}

}
