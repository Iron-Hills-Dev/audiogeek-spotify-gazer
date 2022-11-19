package org.codebusters.audiogeek.spotifygazer.domain.spotify.retrieve;

import org.assertj.core.api.Assertions;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.Artist;
import org.codebusters.audiogeek.spotifygazer.domain.spotify.connection.model.SpotifyArtistResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SpotifyAlbumGenreAnalyzerTest {

    private static final String TOKEN = "test";
    private static final String TEST1_ARTIST1_NAME = "test-single-artist1";
    private static final String TEST2_ARTIST1_NAME = "test-double-artist1";
    private static final String TEST2_ARTIST2_NAME = "test-double-artist2";
    private SpotifyAlbumGenreAnalyzer sut;


    @ParameterizedTest
    @MethodSource("genreAnalyzerCorrectSupplier")
    @DisplayName("genreAnalyzer - should return correct genres for correct artists")
    void genreAnalyzerCorrect(Set<Artist> artists, Set<String> expectedGenres) {
        //given
        sut = SpotifyAlbumGenreAnalyzer.builder()
                .token(TOKEN)
                .artists(artists)
                .artistGenreSupplier(SpotifyAlbumGenreAnalyzerTest::fakeGenreSupplier)
                .build();

        //when
        var genres = sut.getGenre();

        //then
        assertThat(genres).isNotEmpty();
        assertThat(genres).isEqualTo(expectedGenres);
    }

    private static SpotifyArtistResponse fakeGenreSupplier(String token, Artist artist) {
        Assertions.assertThat(token).isEqualTo(TOKEN);
        return switch (artist.id()) {
            case TEST1_ARTIST1_NAME -> SpotifyArtistResponse.builder()
                    .id(artist.id())
                    .genres(List.of("rock", "metal"))
                    .build();
            case TEST2_ARTIST1_NAME -> SpotifyArtistResponse.builder()
                    .id(artist.id())
                    .genres(List.of("rap", "hip-hop"))
                    .build();
            case TEST2_ARTIST2_NAME -> SpotifyArtistResponse.builder()
                    .id(artist.id())
                    .genres(List.of("hip-hop", "trap"))
                    .build();
            default -> null;
        };

    }

    private static Stream<Arguments> genreAnalyzerCorrectSupplier() {
        var singleArtist = Set.of(
                Artist.builder()
                        .id(TEST1_ARTIST1_NAME)
                        .name(TEST1_ARTIST1_NAME)
                        .build()
        );
        var singleArtistGenre = new LinkedHashSet<String>();
        singleArtist.forEach(artist -> singleArtistGenre.addAll(fakeGenreSupplier(TOKEN, artist).genres()));

        var doubleArtist = Set.of(
                Artist.builder()
                        .id(TEST2_ARTIST1_NAME)
                        .name(TEST2_ARTIST1_NAME)
                        .build(),
                Artist.builder()
                        .id(TEST2_ARTIST2_NAME)
                        .name(TEST2_ARTIST2_NAME)
                        .build()
        );
        var doubleArtistGenre = new LinkedHashSet<String>();
        doubleArtist.forEach(artist -> doubleArtistGenre.addAll(fakeGenreSupplier(TOKEN, artist).genres()));

        return Stream.of(
                Arguments.of(singleArtist, singleArtistGenre),
                Arguments.of(doubleArtist, doubleArtistGenre)
        );
    }

}