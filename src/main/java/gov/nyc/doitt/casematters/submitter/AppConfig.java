package gov.nyc.doitt.casematters.submitter;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

	@Value("${rest.template.proxy.enabled:false}")
	private Boolean restTemplateProxyEnabled;

	@Value("${rest.template.proxy.host:}")
	private String restTemplateProxyHost;

	@Value("#{new Integer(${rest.template.proxy.port:0})}")
	private int restTemplateProxyPort;

	@Bean
	public RestTemplate restTemplate() {

		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		if (restTemplateProxyEnabled) {
			Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(restTemplateProxyHost, restTemplateProxyPort));
			requestFactory.setProxy(proxy);
		}
		return new RestTemplate(requestFactory);
	}
}
