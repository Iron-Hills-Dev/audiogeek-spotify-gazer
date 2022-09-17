package org.codebusters.audiogeek.spotifygazer.infrastructure.spotify.connection;

import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.SpotifyConnectionPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
class SpotifyConnectionAdapterConfig {

    @Bean
    SpotifyConnectionPort spotifyConnectionAdapter(@Value("${gazer.spotify.client-id}") String clientId,
                                                   @Value("${gazer.spotify.client-secret}") String clientSecret,
                                                   @Value("${gazer.spotify.base-accounts-url}") String baseAccountsUrl,
                                                   @Value("${gazer.spotify.base-api-url}") String baseApiUrl) {
        var restTemplate = new RestTemplate();

        log.info("Initializing SpotifyConnectionAdapter: baseAccountsUrl={} baseApiUrl={}",
                baseAccountsUrl, baseApiUrl);
        return SpotifyConnectionAdapter.builder()
                .restTemplate(restTemplate)
                .baseAccountsUrl(baseAccountsUrl)
                .baseApiUrl(baseApiUrl)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }
}
