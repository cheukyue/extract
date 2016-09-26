package us.ceka.extract.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;

import us.ceka.extract.util.JsoupTemplate;

@Configuration 
@ComponentScan(basePackages = "us.ceka")
@PropertySource(name = "urlProperties", value = { "classpath:url.properties" })
@PropertySource(value = {"classpath:config.properties"} )
public class AppConfig {
	
	//private static final Logger log = LoggerFactory.getLogger(AppConfig.class);
	
    @Autowired
    private ConfigurableEnvironment env;
    
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }
    
    @Bean
    public JsoupTemplate jsoupTemplate() {
    	JsoupTemplate jt = new JsoupTemplate();
      
    	Map<String, String> endpointMap = new HashMap<String, String>();   	
    	MutablePropertySources propertySources = env.getPropertySources();

    	if(propertySources.get("urlProperties") instanceof EnumerablePropertySource) {
	    	EnumerablePropertySource<?> propertySource = (EnumerablePropertySource<?>) propertySources.get("urlProperties");
	    	for(String key : propertySource.getPropertyNames()) {
	    		Object value = propertySource.getProperty(key); 
	    		if(value instanceof String) endpointMap.put(key, (String)value);
	    	}
    	}
    	jt.setEndpoints(endpointMap);
    	jt.setTimeout(env.getProperty("jsoup.timeout", Integer.class, 3000));
    	jt.setUsaerAgent(env.getRequiredProperty("jsoup.userAgent"));
    	jt.setProxyEnable(env.getProperty("jsoup.proxy.enable", Boolean.class, false));
    	jt.setProxyHost(env.getProperty("jsoup.proxy.host"));
    	jt.setProxyPort(env.getProperty("jsoup.proxy.port", Integer.class, 80));
    	jt.setProxyUser(env.getProperty("jsoup.proxy.user"));
    	jt.setProxyPassword(env.getProperty("jsoup.proxy.password"));
    	
    	return jt;
    }

}
