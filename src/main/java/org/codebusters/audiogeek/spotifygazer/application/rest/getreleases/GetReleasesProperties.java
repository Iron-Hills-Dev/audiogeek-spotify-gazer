package org.codebusters.audiogeek.spotifygazer.application.rest.getreleases;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@Slf4j
@ConfigurationProperties("gazer.app.get-releases")
record GetReleasesProperties(int maxPageSize, int maxGenreLength, int maxGenresSize, Set<String> charBlacklist,
                             Set<String> charWhitelist) {

    @PostConstruct
    void init() {
        log.info("Initialized GetReleasesProperties: {}", this);
    }
}
