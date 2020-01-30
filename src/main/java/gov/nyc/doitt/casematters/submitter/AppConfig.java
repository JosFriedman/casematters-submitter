package gov.nyc.doitt.casematters.submitter;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;

import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		if (restTemplateProxyEnabled) {

			DefaultHttpClient httpClient = (DefaultHttpClient) requestFactory.getHttpClient();
			Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(restTemplateProxyHost, restTemplateProxyPort));
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
		return new RestTemplate(requestFactory);
	}
}
