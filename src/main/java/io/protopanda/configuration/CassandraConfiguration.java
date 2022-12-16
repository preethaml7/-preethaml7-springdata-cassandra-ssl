package io.protopanda.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CassandraConfiguration {

    private final Environment environment;

    private SSLContext createSSLContext() {

        try {

            // define trust store password in property file
            String password = this.environment.getRequiredProperty("cassandra-truststore-password");

            // add jks file under src/main/resources and update the filename as required
            URL url = getClass().getClassLoader().getResource("cassandra-truststore.jks");

            return new SSLContextBuilder()
                    .loadTrustMaterial(url, password.toCharArray())
                    .build();

        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException |
                 KeyManagementException ex) {
            throw new SecurityException("Could not create SSL Context", ex);
        }
    }

    @Bean
    public CqlSessionBuilderCustomizer cqlSessionBuilderCustomizer() {
        return cqlSessionBuilder -> {
            try {
                cqlSessionBuilder.withSslContext(createSSLContext());
            } catch (Exception ex) {
                log.error("Could not establish SSL connection to database", ex);
            }
        };
    }
}
