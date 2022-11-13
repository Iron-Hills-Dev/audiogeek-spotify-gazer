package org.codebusters.audiogeek.spotifygazer.infrastructure.dataexchange;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Slf4j
@RequiredArgsConstructor
@Builder
class DatabaseDataModifyAdapter implements DataModifyPort {

    private final AlbumRepository albumRepo;
    private final ArtistRepository artistRepo;
    private final GenreRepository genreRepo;

    @Override
    @Transactional
    public Optional<UUID> addAlbum(AddAlbumCommand cmd) {
        log.debug("Saving album in database: cmd={}", cmd);
        var album = cmd.album();

        var albumUUID = findAlbumId(album);
        if (albumUUID.isPresent()) {
            log.debug("Album already exists - skipping: id={}", albumUUID);
            return handleAlbumExists();
        }
        return handleCorrectCreation(album);
    }

    @Override
    public void removeAlbum(RemoveAlbumCommand cmd) {

    }

    private Optional<UUID> handleCorrectCreation(Album album) {
        var albumEntity = AlbumEntity.builder()
                .title(album.title())
                .providerId(album.id())
                .releaseDate(album.releaseDate())
                .providerLink(album.link().toString())
                .artists(getArtistEntities(album.artists()))
                .genres(getGenreEntities(album.genres()))
                .build();
        log.trace("Saving album to database...");
        albumEntity = albumRepo.save(albumEntity);
        log.debug("Album saved successfully: id={}", albumEntity.getId());
        return of(albumEntity.getId());
    }

    private Optional<UUID> handleAlbumExists() {
        return empty();
    }

    private Optional<UUID> findAlbumId(Album album) {
        var albumEntity = albumRepo.findByTitleAndReleaseDate(album.title(), album.releaseDate());
        if (albumEntity.isEmpty()) {
            log.trace("Album do not exist yet");
            return empty();
        }
        return of(albumEntity.get().getId());
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
        return artistRepo
                .findByName(artist.name())
                .orElseGet(() -> convertToArtistEntity(artist));
    }

    private GenreEntity findGenreEntity(String genre) {
        return genreRepo
                .findByName(genre)
                .orElseGet(() -> new GenreEntity(genre));
    }

    private ArtistEntity convertToArtistEntity(Artist artist) {
        return new ArtistEntity(artist.id(), artist.name());
    }
}
