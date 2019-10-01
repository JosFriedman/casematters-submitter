package gov.nyc.doitt.casematters.submitter.lm;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jcifs.smb.NtlmPasswordAuthentication;

@Component
public class SmbConfig {

	private Logger logger = LoggerFactory.getLogger(SmbConfig.class);

	@Value("${submitter.smb.domain}")
	private String smbDomain;

	@Value("${submitter.smb.server}")
	private String smbServer;

	@Value("${submitter.smb.userName}")
	private String smbUserName;

	@Value("${submitter.smb.password}")
	private String smbPassword;

	private NtlmPasswordAuthentication smbAuth;

	@PostConstruct
	private void postConstruct() {
		smbAuth = new NtlmPasswordAuthentication(smbDomain, smbUserName, smbPassword);
	}

	public NtlmPasswordAuthentication getSmbAuth() {
		return smbAuth;
	}

	public String getSmbServer() {
		return smbServer;
	}

}
