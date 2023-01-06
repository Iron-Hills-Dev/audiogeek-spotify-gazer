package org.codebusters.audiogeek.spotifygazer.infrastructure.dataexchange;

import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.AddAlbumCommand;
import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.GetAlbumsByGenreFilter;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.Album;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.Artist;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo.AlbumRepository;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo.ArtistRepository;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.time.LocalDate;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.IntStream.rangeClosed;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class DatabaseDataQueryAdapterPagingTest {
    @Autowired
    private AlbumRepository albumRepo;
    @Autowired
    private ArtistRepository artistRepo;
    @Autowired
    private GenreRepository genreRepo;
    @Autowired
    private DatabaseDataModifyAdapter modify;
    @Autowired
    private DatabaseDataQueryAdapter query;

    @BeforeEach
    void createDB() {
        albumRepo.deleteAll();
        artistRepo.deleteAll();
        genreRepo.deleteAll();
        var commands = rangeClosed(1, 100).boxed()
                .map(this::createAlbum)
                .map(AddAlbumCommand::new)
                .collect(toSet());
        commands.forEach(cmd -> modify.addAlbum(cmd));
    }

    private Album createAlbum(Integer i) {
        var artist = new Artist(i.toString(), "artist" + i);
        return Album.builder()
                .id(i.toString())
                .link(URI.create("album%s.com".formatted(i)))
                .title("album" + i)
                .releaseDate(LocalDate.of(1900 + i, (12 % i == 0) ? 1 : 12 % i, (25 % i == 0) ? 1 : 25 % i))
                .artists(Set.of(artist))
                .genres(Set.of((i % 2 == 0) ? "genre2" : "genre1"))
                .build();
    }

    @Test
    @DisplayName("getAlbum - check if pagination works")
    void getAlbumIterate() {
        var albums = query.getAlbums(PageRequest.of(0, 10));
        int total = albums.getNumberOfElements();
        while (albums.hasNext()) {
            albums = query.getAlbums(albums.nextPageable());
            total += albums.getNumberOfElements();
        }
        assertThat(total).isEqualTo(100);
    }

    @Test
    @DisplayName("getAlbum (by genre) - check if pagination works")
    void getAlbumByGenreIterate() {
        var albums = query.getAlbums(new GetAlbumsByGenreFilter(Set.of("genre2"), PageRequest.of(0, 5)));
        int total = albums.getNumberOfElements();
        while (albums.hasNext()) {
            albums = query.getAlbums(new GetAlbumsByGenreFilter(Set.of("genre2"), albums.nextPageable()));
            total += albums.getNumberOfElements();
        }
        assertThat(total).isEqualTo(50);
    }
}
