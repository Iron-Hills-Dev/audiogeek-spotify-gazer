package org.codebusters.audiogeek.spotifygazer.application.getreleases;

import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.GetAlbumsByGenreFilter;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.Album;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.Artist;
import org.codebusters.audiogeek.spotifygazer.infrastructure.dataexchange.DatabaseDataQueryAdapter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GetReleasesRestAdapter.class)
@Slf4j
public class GetReleasesRestAdapterTest {

    private static final Path POST_CONTENT = Path.of("src/test/resources/application/getreleases/post_content.json");
    private static final Path CORRECT_RESULT = Path.of("src/test/resources/application/getreleases/correct_result.json");
    private static final Path SIZE_EXCEEDED_RESULT = Path.of("src/test/resources/application/getreleases/size_exceeded_result.json");

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

    @Test
    public void getCurrentReleasesTestCorrect() throws Exception {
        // given
        var pageable = PageRequest.of(0, 2);
        var filter = new GetAlbumsByGenreFilter(Set.of("rock"), pageable);
        var releases = new PageImpl<>(getAlbums(), pageable, 6);
        given(queryMock.getAlbums(filter)).willReturn(releases);

        var postContent = Files.readString(POST_CONTENT);
        var expected = Files.readString(CORRECT_RESULT);

        // when & then
        mvc.perform(post("/api/v1/get-current-releases?page=0&size=2")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(postContent))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    public void getCurrentReleasesTestPageSizeExceeded() throws Exception {
        // given
        var postContent = Files.readString(POST_CONTENT);
        var expected = Files.readString(SIZE_EXCEEDED_RESULT);

        // when & then
        mvc.perform(post("/api/v1/get-current-releases?page=0&size=50")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(postContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expected));
    }
}
