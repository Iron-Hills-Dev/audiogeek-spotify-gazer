package org.codebusters.audiogeek.spotifygazer.application.rest.getreleases;

import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.spotifygazer.application.rest.exception.ApiErrorData;
import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.GetAlbumsByGenreFilter;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.Album;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.Artist;
import org.codebusters.audiogeek.spotifygazer.infrastructure.dataexchange.DatabaseDataQueryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Nullable;
import java.net.URI;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.nio.file.Files.readString;
import static org.codebusters.audiogeek.spotifygazer.application.rest.exception.GlobalApiErrorData.ARGUMENT_TYPE_MISMATCH;
import static org.codebusters.audiogeek.spotifygazer.application.rest.getreleases.exception.GetReleasesApiErrorData.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class GetReleasesRestAdapterTest {
    static final String PATH_PREFIX = "src/test/resources/application/rest/getreleases/";
    private static final Path POST_CONTENT = Path.of(PATH_PREFIX + "post_content.json");
    private static final Path CORRECT_RESULT = Path.of(PATH_PREFIX + "correct_result.json");
    private static final Path EMPTY_GENRES_CON = Path.of(PATH_PREFIX + "empty_genres_con.json");
    private static final Path EMPTY_GENRE_CON = Path.of(PATH_PREFIX + "empty_genre_con.json");
    private static final Path TOO_LONG_GENRE_CON = Path.of(PATH_PREFIX + "too_long_genre_con.json");
    private static final Path TOO_MANY_GENRES_CON = Path.of(PATH_PREFIX + "too_many_genres_con.json");
    private static final Path NULL_GENRES_CON = Path.of(PATH_PREFIX + "null_genres_con.json");

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DatabaseDataQueryAdapter queryMock;

    private List<Album> getAlbums() {
        var artists1 = Set.of(Artist.builder()
                .id("123")
                .name("Iggy Pop")
                .build());
        var artists2 = Set.of(Artist.builder()
                .id("456")
                .name("Bruce Springsteen")
                .build());
        var album1 = Album.builder()
                .title("EVERY LOSER")
                .artists(artists1)
                .id("123")
                .releaseDate(LocalDate.of(2023, 1, 6))
                .link(URI.create("https://open.spotify.com/album/62VSZ71LvrUh1VoSuPgzXd"))
                .genres(Set.of("rock", "punk"))
                .build();
        var album2 = Album.builder()
                .title("Only the Strong Survive")
                .artists(artists2)
                .id("456")
                .releaseDate(LocalDate.of(2022, 11, 11))
                .link(URI.create("https://open.spotify.com/album/4XJaXh57G3rZtAzqeVZSfn"))
                .genres(Set.of("rock", "classic rock"))
                .build();
        return List.of(album1, album2);
    }

    @DisplayName("Correct getCurrentReleases flow test")
    @Test
    public void getCurrentReleasesTestCorrect() throws Exception {
        // given
        var pageable = PageRequest.of(0, 2);
        var filter = new GetAlbumsByGenreFilter(Set.of("rock"), pageable);
        var releases = new PageImpl<>(getAlbums(), pageable, 6);
        doReturn(releases).when(queryMock).getAlbums(filter);

        // when & then
        mvc.perform(post("/api/v1/get-current-releases?page=0&size=2")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(readString(POST_CONTENT)))
                .andExpect(status().isOk())
                .andExpect(content().json(readString(CORRECT_RESULT)));
    }

    @DisplayName("Test if exceptions are handled for wrong arguments")
    @ParameterizedTest(name = "{index}. {2} (page={0} size={1})")
    @MethodSource("provideWrongArguments")
    public void getCurrentReleasesTestWrongArguments(String page, String size, ApiErrorData expectedError, @Nullable String message) throws Exception {
        // when & then
        mvc.perform(post("/api/v1/get-current-releases?page=%s&size=%s".formatted(page, size))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(readString(POST_CONTENT)))
                .andExpect(status().is(expectedError.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(expectedError.getCode()))
                .andExpect(jsonPath("$.message").value((message == null) ? expectedError.getMessage() : message));
    }

    @DisplayName("Test if exceptions are handled for wrong genres (except illegal symbols)")
    @ParameterizedTest(name = "{index}. {1}")
    @MethodSource("provideWrongGenres")
    public void getCurrentReleasesTestWrongGenres(Path con, ApiErrorData expectedError, @Nullable String message) throws Exception {
        // when & then
        mvc.perform(post("/api/v1/get-current-releases?page=0&size=2")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(readString(con)))
                .andExpect(status().is(expectedError.getHttpStatus().value()))
                .andExpect(jsonPath("$.code").value(expectedError.getCode()))
                .andExpect(jsonPath("$.message").value((message == null) ? expectedError.getMessage() : message));
    }

    public static Stream<Arguments> provideWrongArguments() {
        return Stream.of(
                of("0", "50", PAGE_SIZE_EXCEEDED, PAGE_SIZE_EXCEEDED.getMessage().formatted(20)),
                of("0", "0", WRONG_PAGE_SIZE, null),
                of("0", "-5", WRONG_PAGE_SIZE, null),
                of("-3", "10", WRONG_PAGE_INDEX, null),
                of("a", "10", ARGUMENT_TYPE_MISMATCH, ARGUMENT_TYPE_MISMATCH.getMessage().formatted("page", "int")),
                of("0", "a", ARGUMENT_TYPE_MISMATCH, ARGUMENT_TYPE_MISMATCH.getMessage().formatted("size", "int"))
        );
    }

    public static Stream<Arguments> provideWrongGenres() {
        return Stream.of(
                of(NULL_GENRES_CON, GENRES_NULL, null),
                of(EMPTY_GENRE_CON, GENRE_EMPTY, null),
                of(EMPTY_GENRES_CON, GENRES_EMPTY, null),
                of(TOO_LONG_GENRE_CON, GENRE_TOO_LONG, GENRE_TOO_LONG.getMessage().formatted("aaaaaaaaaaa", 10)),
                of(TOO_MANY_GENRES_CON, GENRES_TOO_LONG, GENRES_TOO_LONG.getMessage().formatted(5))
        );
    }
}
