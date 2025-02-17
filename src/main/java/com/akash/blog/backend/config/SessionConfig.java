package com.akash.blog.backend.config;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionConfig {

    @Bean
    public DefaultCookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("JSESSIONID");
        serializer.setCookiePath("/");  // Default path
        serializer.setSameSite("None"); // Required for cross-origin cookies
        serializer.setUseSecureCookie(true);
        return serializer;
    }
}
