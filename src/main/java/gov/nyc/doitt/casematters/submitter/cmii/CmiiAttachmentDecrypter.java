package gov.nyc.doitt.casematters.submitter.cmii;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CmiiAttachmentDecrypter {

	private Logger logger = LoggerFactory.getLogger(CmiiAttachmentDecrypter.class);

	public String decrypt(String encryptedFileName) throws IOException {
		
		String decryptedFileName = String.format("%s-decrypted", FilenameUtils.getBaseName(encryptedFileName));
		File decryptedFile = File.createTempFile(decryptedFileName, null);
		InputStream is = new FileInputStream(new File(encryptedFileName));
		OutputStream os = new FileOutputStream(decryptedFile);
		IOUtils.copy(is, os);
		is.close();
		os.close();
		
		return decryptedFile.getPath();
	}
}