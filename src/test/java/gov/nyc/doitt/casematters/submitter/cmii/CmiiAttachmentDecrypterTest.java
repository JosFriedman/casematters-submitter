package gov.nyc.doitt.casematters.submitter.cmii;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import gov.nyc.doitt.casematters.submitter.TestBase;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CmiiAttachmentDecrypterTest extends TestBase {

	@Value("${submitter.cmmii.decryption.keystoreFileName}")
	private String keystoreFileName;

	@Value("${submitter.cmmii.decryption.keystorePassword}")
	private String keystorePassword;

	@Value("${submitter.cmmii.decryption.key}")
	private String key;

	@Value("${submitter.cmmii.decryption.keyPassword}")
	private String keyPassword;

	@InjectMocks
	private CmiiAttachmentDecrypter cmiiAttachmentDecrypter = new CmiiAttachmentDecrypter();

	@Before
	public void setup() throws Exception {
		FieldUtils.writeField(cmiiAttachmentDecrypter, "keystoreFileName", keystoreFileName, true);
		FieldUtils.writeField(cmiiAttachmentDecrypter, "keystorePassword", keystorePassword, true);
		FieldUtils.writeField(cmiiAttachmentDecrypter, "key", key, true);
		FieldUtils.writeField(cmiiAttachmentDecrypter, "keyPassword", keyPassword, true);
		ReflectionTestUtils.invokeMethod(cmiiAttachmentDecrypter, "init");
	}

	@After
	public void teardown() throws Exception {
	}

	@Test
	public void testDecrypt() throws Exception {

		String encryptedFileName = "cmiAttachment-encrypted.dat";
		File file = new File(getClass().getClassLoader().getResource(encryptedFileName).toURI());

		String decryptedFileName = cmiiAttachmentDecrypter.decrypt(file.getAbsolutePath());
		assertNotNull(decryptedFileName);
	}

}
