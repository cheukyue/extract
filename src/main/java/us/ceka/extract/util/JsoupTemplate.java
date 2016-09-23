package us.ceka.extract.util;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JsoupTemplate {
	private static final Logger log = LoggerFactory.getLogger(JsoupTemplate.class);

	private int timeout;
	private String usaerAgent;
	private Map<String, String> endpoints;
	private boolean proxyEnable;
	private String proxyHost;
	private Integer proxyPort;
	private String proxyUser;
	private String proxyPassword;

	public JsoupTemplate setTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}
	public void setUsaerAgent(String usaerAgent) {
		this.usaerAgent = usaerAgent;
	}
	public void setProxyEnable(boolean proxyEnable) {
		this.proxyEnable = proxyEnable;
	}
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}
	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}
	public void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
	}
	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}
	public JsoupTemplate setEndpoints(Map<String, String> endpoints) {
		this.endpoints = endpoints;
		return this;
	}
	public int getTimeout() {
		return timeout;
	}
	public Map<String, String> getEndpoints() {
		return endpoints;
	}

	public Document getDocumnetByAlias(String alias) {
		return getDocumnetByAlias(alias, new Object[]{});
	}

	public Document getDocumnetByAlias(String alias, Object... args) {
		String connectUrl = args.length > 0 ? String.format(endpoints.get(alias), args) : endpoints.get(alias) ;	
		return getDocument(connectUrl);
	}

	public Document getDocument(String url) {
		Document documnet = null;
		log.info("Connecting to '{}'...", url);
		try {
			Connection connection = Jsoup.connect(url).timeout(timeout).userAgent(usaerAgent);
			if(proxyEnable) {
				Authenticator.setDefault(new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return (new PasswordAuthentication(proxyUser, proxyPassword.toCharArray()));
					}
				});
				connection.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));
			}
				
			//if(this.cookies != null) connection.cookies(cookies);
			Connection.Response response = connection.execute();
			documnet = response.parse();

		} catch (IOException e) {
			log.error("Error in retrieving Jsoup document", e);
		}
		return documnet;		
	}
	@Override
	public String toString() {
		return String.format("JsoupTemplate [timeout:%d]", timeout);
	}
}
