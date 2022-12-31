package org.codebusters.audiogeek.spotifygazer.infrastructure.dataexchange;

import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo.AlbumRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
class DatabaseDataQueryAdapterConfig {

    @Bean
    DatabaseDataQueryAdapter databaseDataQueryAdapter(AlbumRepository albumRepo) {
        log.info("Initializing DatabaseDataQueryAdapter");
        return new DatabaseDataQueryAdapter(albumRepo);
    }
}
