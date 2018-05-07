package zielichowski.reactive.api.config;

import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApiConfiguration {
    private static final String VND_GITHUB_V3 = "application/vnd.github.v3+json";
    private static final String USER_AGENT = "Tomasz Agent";


    @Bean
    public WebClient restTemplate() {

        ReactorClientHttpConnector connector =
                new ReactorClientHttpConnector(options -> {
                    options.option(ChannelOption.SO_TIMEOUT, 2000);
                    options.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000);

                });

        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, VND_GITHUB_V3)
                .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
                .clientConnector(connector)
                .build();

    }
}

