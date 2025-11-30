package ru.example.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class TomcatConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    private static final Logger log = LoggerFactory.getLogger(TomcatConfig.class);

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addConnectorCustomizers(connector -> {
            // Устанавливаем relaxed символы
            connector.setProperty("relaxedQueryChars", "[]{}(),$=+");
            connector.setProperty("relaxedPathChars", "[]{}(),$=+");

            // Выводим их в лог
            log.info("relaxedQueryChars set to: {}", connector.getProperty("relaxedQueryChars"));
            log.info("relaxedPathChars set to: {}", connector.getProperty("relaxedPathChars"));
        });
    }
}
