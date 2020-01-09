package gov.nyc.doitt.casematters.submitter.cmii;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.cms.CMSEnvelopedDataParser;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipient;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CmiiAttachmentDecrypter {

	private Logger logger = LoggerFactory.getLogger(CmiiAttachmentDecrypter.class);

	@Value("${submitter.cmmii.decryption.keystoreFileName}")
	private String keystoreFileName;

	@Value("${submitter.cmmii.decryption.keystorePassword}")
	private String keystorePassword;

	@Value("${submitter.cmmii.decryption.certificateName}")
	private String certificateName;

	@Value("${submitter.cmmii.decryption.key}")
	private String key;

	@Value("${submitter.cmmii.decryption.keyPassword}")
	private String keyPassword;

	private X509Certificate x509Certificate;

	private PrivateKey privateKey;

	@PostConstruct
	private void init()
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {

		InputStream keystoreStream = getClass().getClassLoader().getResourceAsStream(keystoreFileName);
		KeyStore keystore = KeyStore.getInstance("JKS");
		keystore.load(keystoreStream, keystorePassword.toCharArray());

		// certificate
		x509Certificate = (X509Certificate) keystore.getCertificate(certificateName);
		if (x509Certificate == null) {
			throw new CertificateException("Can't get X509Certificate");
		}

		// private key
		Security.addProvider(new BouncyCastleProvider());
		privateKey = (PrivateKey) keystore.getKey(key, keyPassword.toCharArray());
		if (privateKey == null) {
			throw new KeyStoreException("Can't get PrivateKey");
		}
	}

	public String decrypt(String encryptedFileName) {

		try {
			String decryptedFileName = String.format("%s-decrypted", FilenameUtils.getBaseName(encryptedFileName));
			File decryptedFile = File.createTempFile(decryptedFileName, null);
			try (InputStream encryptedStream = new FileInputStream(new File(encryptedFileName));
					OutputStream os = new FileOutputStream(decryptedFile)) {

				CMSEnvelopedDataParser edp = new CMSEnvelopedDataParser(encryptedStream);
				RecipientInformation recipientInfo = (RecipientInformation) edp.getRecipientInfos().getRecipients().toArray()[0];
				JceKeyTransEnvelopedRecipient jceKeyTransEnvelopedRecipient = new JceKeyTransEnvelopedRecipient(privateKey);
				JceKeyTransRecipient jceKeyTransRecipient = jceKeyTransEnvelopedRecipient
						.setProvider(BouncyCastleProvider.PROVIDER_NAME);
				try (InputStream is = recipientInfo.getContentStream(jceKeyTransRecipient).getContentStream()) {
					IOUtils.copy(is, os);
				}
				return decryptedFile.getPath();
			}
		} catch (Exception e) {
			throw new CmiiSubmitterException("Can't decrypt: " + encryptedFileName, e);
		}
	}

}