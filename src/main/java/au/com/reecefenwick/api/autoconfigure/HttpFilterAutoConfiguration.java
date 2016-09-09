package au.com.reecefenwick.api.autoconfigure;

import au.com.reecefenwick.api.rest.filter.HttpRequestResponseMetricFilter;
import au.com.reecefenwick.api.rest.filter.RequestIdFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpFilterAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(HttpFilterAutoConfiguration.class);

    @Bean
    public FilterRegistrationBean requestIdFilter() {
        log.info("Configuring request ID filter");
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new RequestIdFilter());
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean httpRequestResponseMetricFilter() {
        log.info("Configuring Request/Response Metric filter");
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new HttpRequestResponseMetricFilter());
        return registrationBean;
    }
}
