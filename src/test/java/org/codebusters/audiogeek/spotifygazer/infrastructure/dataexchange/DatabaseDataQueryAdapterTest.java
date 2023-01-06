package org.codebusters.audiogeek.spotifygazer.infrastructure.dataexchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.AddAlbumCommand;
import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.GetAlbumsByGenreFilter;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.Album;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo.AlbumRepository;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo.ArtistRepository;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class DatabaseDataQueryAdapterTest {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(WRITE_DATES_AS_TIMESTAMPS);

    private final List<Album> albums = new LinkedList<>();

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
    void createDB() throws IOException {
        albumRepo.deleteAll();
        artistRepo.deleteAll();
        genreRepo.deleteAll();
        for (int i = 1; i <= 5; i++) {
            var album = MAPPER.readValue(Path.of("src/test/resources/dataexchange/exampleAlbums/album%s.json".formatted(i)).toFile(), Album.class);
            modify.addAlbum(new AddAlbumCommand(album));
            albums.add(album);
        }
    }

    @Test
    @DisplayName("getAlbums - check if sorting is correct")
    void getAllAlbumsSortTest() {
        var releaseDates = query.getAlbums(PageRequest.of(0, 5)).stream()
                .map(Album::releaseDate)
                .toList();
        var sorted = releaseDates.stream()
                .sorted(comparing(LocalDate::toString).reversed())
                .toList();
        assertThat(releaseDates).isEqualTo(sorted);
    }

    @Test
    @DisplayName("getAlbums - check if return data is correct")
    void getAllAlbumsCheckDataCorrectness() {
        var releaseDates = query.getAlbums(PageRequest.of(0, 5)).stream()
                .sorted(comparing(Album::id)).toList();
        assertThat(releaseDates).usingRecursiveComparison().isEqualTo(albums);
    }

    @Test
    @DisplayName("getAlbums (by genre) - check if sorting is correct")
    void getAlbumsByGenreSortTest() {
        var releaseDates = query.getAlbums(new GetAlbumsByGenreFilter(Set.of("genre1", "genre2", "genre3"), PageRequest.of(0, 5)))
                .stream()
                .map(Album::releaseDate)
                .toList();
        var sorted = releaseDates.stream()
                .sorted(comparing(LocalDate::toString).reversed())
                .toList();
        assertThat(releaseDates).isEqualTo(sorted);
    }

    @ParameterizedTest
    @CsvSource({"genre1", "genre2;genre3", "genre2", "genre1;genre3", "genre4"})
    @DisplayName("getAlbums (by genre) - check if return data is correct")
    void getAlbumsByGenreDataCorrectness(String input) {
        // given
        Set<String> genres = Stream.of(input.trim().split("\\s*;\\s*"))
                .collect(toSet());

        // when
        var releases = query.getAlbums(new GetAlbumsByGenreFilter(genres, PageRequest.of(0, 5))).stream()
                .sorted(comparing(Album::id))
                .toList();
        var correct = albums.stream()
                .filter(a -> CollectionUtils.containsAny(a.genres(), genres))
                .sorted(comparing(Album::id))
                .toList();

        // then
        assertThat(releases).usingRecursiveComparison().isEqualTo(correct);
    }
}
