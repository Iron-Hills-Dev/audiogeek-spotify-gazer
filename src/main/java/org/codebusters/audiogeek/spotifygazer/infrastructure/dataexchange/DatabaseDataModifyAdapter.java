package org.codebusters.audiogeek.spotifygazer.infrastructure.dataexchange;

import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.DataModifyPort;
import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.AddAlbumCommand;
import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.RemoveAlbumCommand;
import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model.Album;
import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model.Artist;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.AlbumEntity;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.ArtistEntity;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.GenreEntity;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo.AlbumRepository;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo.ArtistRepository;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
class DatabaseDataModifyAdapter implements DataModifyPort {

    @Autowired
    private AlbumRepository albumRepo;
    @Autowired
    private ArtistRepository artistRepo;
    @Autowired
    private GenreRepository genreRepo;

    @Override
    @Transactional
    public UUID addAlbum(AddAlbumCommand cmd) {
        log.debug("Saving album in database: cmd={}", cmd);
        var album = cmd.album();

        var albumUUID = findAlbum(album);
        if (albumUUID.isPresent()) {
            log.info("Album already exists - skipping: album={}", album);
            return albumUUID.get();
        }

        var albumEntity = AlbumEntity.builder()
                .title(album.title())
                .providerId(album.id())
                .releaseDate(album.releaseDate())
                .providerLink(album.link().toString())
                .artists(getArtistEntities(album.artists()))
                .genres(getGenreEntities(album.genres()))
                .build();
        log.trace("Starting database transaction...");
        albumEntity = albumRepo.save(albumEntity);
        log.info("Album saved successfully: album={}", albumEntity);
        return albumEntity.getId();
    }

    @Override
    public void removeAlbum(RemoveAlbumCommand cmd) {

    }

    private Optional<UUID> findAlbum(Album album) {
        var albumEntity = albumRepo.findByTitleAndReleaseDate(album.title(), album.releaseDate());
        if (albumEntity.isEmpty()) {
            log.trace("Album do not exist yet");
            return Optional.empty();
        }
        return Optional.of(albumEntity.get().getId());
    }

    private Set<ArtistEntity> getArtistEntities(Set<Artist> artists) {
        log.trace("Getting artist entities set");
        return artists.stream()
                .map(this::findArtistEntity)
                .collect(Collectors.toSet());
    }

    private Set<GenreEntity> getGenreEntities(Set<String> genres) {
        log.trace("Getting genre entities set");
        return genres.stream()
                .map(this::findGenreEntity)
                .collect(Collectors.toSet());
    }

    private ArtistEntity findArtistEntity(Artist artist) {
        var artistEntity = artistRepo.findByName(artist.name());
        return artistEntity.orElse(artist.convertToArtistEntity());
    }

    private GenreEntity findGenreEntity(String genre) {
        var genreEntity = genreRepo.findByName(genre);
        return genreEntity.orElse(new GenreEntity(genre));
    }
}
