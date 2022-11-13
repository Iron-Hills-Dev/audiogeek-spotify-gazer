package org.codebusters.audiogeek.spotifygazer.infrastructure.dataexchange;

import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.AddAlbumCommand;
import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model.Album;
import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model.Artist;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo.AlbumRepository;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo.ArtistRepository;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.time.LocalDate;
import java.util.Set;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class DatabaseDataModifyAdapterTest {

    @Autowired
    private AlbumRepository albumRepo;
    @Autowired
    private ArtistRepository artistRepo;
    @Autowired
    private GenreRepository genreRepo;
    @Autowired
    private DatabaseDataModifyAdapter adapter;

    @BeforeEach
    void clearDatabase() {
        albumRepo.deleteAll();
        artistRepo.deleteAll();
        genreRepo.deleteAll();
    }

    @Test
    void addAlbumCorrect() {
        // given
        var album = createTestAlbum();

        // when
        var albumId = adapter.addAlbum(new AddAlbumCommand(album));
        assertThat(albumId).isNotEmpty();

        // then
        var albumEntity = albumRepo.findById(albumId.get());
        assertThat(albumEntity).isNotEmpty();
        assertThat(albumEntity.get().getId()).isEqualTo(albumId.get());

    }

    @Test
    void addAlbumDuplicate() {
        // given
        var album = createTestAlbum();

        // when
        adapter.addAlbum(new AddAlbumCommand(album));
        var albumId = adapter.addAlbum(new AddAlbumCommand(album));

        // then
        assertThat(albumId).isEmpty();
    }

    private Album createTestAlbum() {
        var artists = Set.of(
                Artist.builder()
                        .id("a1")
                        .name("artist1")
                        .build(),
                Artist.builder()
                        .id("a2")
                        .name("artist2")
                        .build()
        );
        var genres = Set.of("rock", "metal", "heavy metal");
        return Album.builder()
                .id("123456")
                .title("test-album")
                .artists(artists)
                .genres(genres)
                .link(URI.create("https://open.spotify.com/album/123456"))
                .releaseDate(LocalDate.parse("2000-12-12", ofPattern("yyyy-MM-dd")))
                .build();
    }
}