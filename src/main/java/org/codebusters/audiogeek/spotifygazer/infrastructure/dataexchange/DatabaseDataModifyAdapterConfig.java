package org.codebusters.audiogeek.spotifygazer.infrastructure.dataexchange;

import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo.AlbumRepository;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo.ArtistRepository;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo.GenreRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
class DatabaseDataModifyAdapterConfig {

    @Bean
    DatabaseDataModifyAdapter databaseDataModifyAdapter(AlbumRepository albumRepo,
                                                        GenreRepository genreRepo,
                                                        ArtistRepository artistRepo) {
        log.info("Initializing DatabaseDataModifyAdapter");
        return DatabaseDataModifyAdapter.builder()
                .albumRepo(albumRepo)
                .genreRepo(genreRepo)
                .artistRepo(artistRepo)
                .build();
    }
}
