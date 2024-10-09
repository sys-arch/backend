package com.config; 

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLException;
import java.io.File;

@Configuration
public class NettyServerConfig {

    @Bean
    public WebServerFactoryCustomizer<NettyReactiveWebServerFactory> nettyCustomizer() throws SSLException {
        return server -> {
            // Rutas a los archivos PEM
            File certChainFile = new File("src/main/resources/certificates/ca-bundle.pem");  
            File privateKeyFile = new File("src/main/resources/certificates/private-key.pem");  

            // Crear el contexto SSL usando Netty y los archivos PEM
            SslContext sslContext = SslContextBuilder.forServer(certChainFile, privateKeyFile).build();

            // Configurar Netty para usar el contexto SSL
            server.addServerCustomizers(builder -> builder.sslContext(sslContext));
        };
    }
}
