package io.protopanda.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.net.ssl.SSLContext;
import java.net.URL;

@Configuration
@RequiredArgsConstructor
public class CassandraConfiguration {

    private final Environment environment;

    @Bean
    public CqlSessionBuilderCustomizer cqlSessionBuilderCustomizer() {

        SSLContext sslContext;

        // define trust store password in property file
        String password = this.environment.getRequiredProperty("cassandra-truststore-password");

        // add jks file under src/main/resources and update the filename as required
        URL url = getClass().getClassLoader().getResource("cassandra-truststore.jks");

        try {

            sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(url, password.toCharArray())
                    .build();

        } catch (Exception ex) {
            throw new SecurityException("Could not establish SSL connection to database");
        }

        return cqlSessionBuilder -> cqlSessionBuilder.withSslContext(sslContext);
    }
}
