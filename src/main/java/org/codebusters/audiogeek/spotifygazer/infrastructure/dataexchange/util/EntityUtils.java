package org.codebusters.audiogeek.spotifygazer.infrastructure.dataexchange.util;

import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.Album;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.Artist;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.AlbumEntity;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.ArtistEntity;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.GenreEntity;

import java.net.URI;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;

public class EntityUtils {
    public static AlbumEntity convertToEntity(Album album, UUID albumId) {
        return AlbumEntity.builder()
                .id(albumId)
                .providerId(album.id())
                .title(album.title())
                .providerLink(album.link().toString())
                .releaseDate(album.releaseDate())
                .artists(convertToArtistEntities(album.artists()))
                .genres(convertToGenreEntities(album.genres()))
                .build();
    }

    public static Album convertToAlbum(AlbumEntity album) {
        return Album.builder()
                .title(album.getTitle())
                .id(album.getProviderId())
                .link(URI.create(album.getProviderLink()))
                .releaseDate(album.getReleaseDate())
                .artists(convertToArtists(album.getArtists()))
                .genres(convertToGenres(album.getGenres()))
                .build();
    }

    private static Set<String> convertToGenres(Set<GenreEntity> genres) {
        return genres.stream()
                .map(GenreEntity::getName)
                .collect(toSet());
    }

    private static Set<Artist> convertToArtists(Set<ArtistEntity> artists) {
        return artists.stream()
                .map(a -> Artist.builder()
                        .id(a.getProviderId())
                        .name(a.getName())
                        .build())
                .collect(toSet());
    }

    private static Set<ArtistEntity> convertToArtistEntities(Set<Artist> artists) {
        return artists.stream()
                .map(ArtistEntity::fromArtist)
                .collect(toSet());
    }

    private static Set<GenreEntity> convertToGenreEntities(Set<String> genres) {
        return genres.stream()
                .map(GenreEntity::new)
                .collect(toSet());
    }
}
