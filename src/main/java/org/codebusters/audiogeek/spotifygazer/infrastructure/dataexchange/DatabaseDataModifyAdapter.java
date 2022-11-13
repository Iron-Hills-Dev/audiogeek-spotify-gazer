package org.codebusters.audiogeek.spotifygazer.infrastructure.dataexchange;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.DataModifyPort;
import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.AddAlbumCommand;
import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.RemoveAlbumCommand;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.Album;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.Artist;
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

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toSet;
import static org.codebusters.audiogeek.spotifygazer.infrastructure.db.ArtistEntity.fromArtist;

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
        log.debug("Processing add album command: cmd={}", cmd);
        var album = cmd.album();

        var albumUUID = findAlbumId(album);
        if (albumUUID.isPresent()) {
            return handleAlbumExists(albumUUID.get());
        }
        return handleAlbumCreation(album);
    }

    @Override
    public void removeAlbum(RemoveAlbumCommand cmd) {

    }

    private Optional<UUID> handleAlbumCreation(Album album) {
        var albumEntity = AlbumEntity.builder()
                .title(album.title())
                .providerId(album.id())
                .releaseDate(album.releaseDate())
                .providerLink(album.link().toString())
                .artists(getArtistEntities(album.artists()))
                .genres(getGenreEntities(album.genres()))
                .build();
        albumEntity = albumRepo.save(albumEntity);
        log.debug("Album saved successfully: id={}", albumEntity.getId());
        return of(albumEntity.getId());
    }

    private Optional<UUID> handleAlbumExists(UUID albumUUID) {
        log.debug("Album already exists - skipping: id={}", albumUUID);
        return empty();
    }

    private Optional<UUID> findAlbumId(Album album) {
        return albumRepo.findByTitleAndReleaseDate(album.title(), album.releaseDate())
                .map(AlbumEntity::getId);
    }

    private Set<ArtistEntity> getArtistEntities(Set<Artist> artists) {
        log.trace("Getting artist entities set");
        return artists.stream()
                .map(this::findOrCreateNewArtistEntity)
                .collect(toSet());
    }

    private Set<GenreEntity> getGenreEntities(Set<String> genres) {
        log.trace("Getting genre entities set");
        return genres.stream()
                .map(this::findOrCreateNewGenreEntity)
                .collect(toSet());
    }

    private ArtistEntity findOrCreateNewArtistEntity(Artist artist) {
        return artistRepo.findByNameAndProviderId(artist.name(), artist.id())
                .orElseGet(() -> fromArtist(artist));
    }

    private GenreEntity findOrCreateNewGenreEntity(String genre) {
        return genreRepo.findByName(genre)
                .orElseGet(() -> new GenreEntity(genre));
    }

}
