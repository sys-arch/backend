package com.equipo3.reuneme;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addConnectorCustomizers((connector) -> {
            connector.setPort(8442);
            connector.setSecure(true);
            connector.setScheme("https");
            connector.setRedirectPort(8442);
        });
    }
}