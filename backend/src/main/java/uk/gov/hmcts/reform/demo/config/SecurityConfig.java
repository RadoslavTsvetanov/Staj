package uk.gov.hmcts.reform.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.demo.filters.AuthTokenFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private AuthTokenFilter authTokenFilter;

    @Bean
    public FilterRegistrationBean<AuthTokenFilter> loggingFilter(){
        FilterRegistrationBean<AuthTokenFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(authTokenFilter);
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}

