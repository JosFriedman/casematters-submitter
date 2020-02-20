package gov.nyc.doitt.casematters.submitter.lm;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.apache.commons.net.ftp.FTPSClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import gov.nyc.doitt.casematters.submitter.TestBase;
import gov.nyc.doitt.casematters.submitter.cmii.CmiiAttachmentDecrypter;
import gov.nyc.doitt.casematters.submitter.cmii.CmiiAttachmentRetriever;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmission;
import gov.nyc.doitt.casematters.submitter.lm.model.LmSubmissionMockerUpper;
import jcifs.smb.NtlmPasswordAuthentication;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LmAttachmentUploaderTest extends TestBase {

	@Mock
	private CmiiAttachmentDecrypter cmiiAttachmentDecrypter;

	@Mock
	private CmiiAttachmentRetriever cmiiAttachmentRetriever;

	@Mock
	private SmbConfig smbConfig;

	@Mock
	private LmAttachmentConfig lmAttachmentConfig;

	@Autowired
	private LmSubmissionMockerUpper lmSubmissionMockerUpper;

	@InjectMocks
	private LmAttachmentUploader lmAttachmentUploader = new LmAttachmentUploader();

	@Before
	public void setup() throws Exception {
	}

	@After
	public void teardown() throws Exception {
	}

	// TODO: no successful test due to not mocking up a SMB file service at this time

	@Test
	public void testUpload_FailLogin() throws Exception {

		LmSubmission lmSubmission = lmSubmissionMockerUpper.create();

		FTPSClient ftpsClient = new FTPSClient();
		when(cmiiAttachmentRetriever.open()).thenReturn(ftpsClient);

		NtlmPasswordAuthentication ntlmPasswordAuthentication = new NtlmPasswordAuthentication("smbDomain", "smbUserName",
				"smbPassword");
		when(smbConfig.getSmbAuth()).thenReturn(ntlmPasswordAuthentication);

		String encryptedFileName = "cmiAttachment-encrypted.dat";
		File file = new File(getClass().getClassLoader().getResource(encryptedFileName).toURI());
		when(cmiiAttachmentRetriever.retrieveFile(any(FTPSClient.class), anyString(), anyString())).thenReturn(file);

		try {
			lmAttachmentUploader.upload(lmSubmission);
			assertTrue(false);
		} catch (LmSubmitterException e) {
			assertTrue(e.getMessage().contains("jcifs.smb.SmbAuthException: Logon failure: unknown user name or bad password."));
		}

		verify(cmiiAttachmentRetriever).open();
		verify(cmiiAttachmentRetriever).close(any(FTPSClient.class));
		verify(cmiiAttachmentRetriever).retrieveFile(any(FTPSClient.class), anyString(), anyString());
		verify(smbConfig).getSmbAuth();
	}

}
