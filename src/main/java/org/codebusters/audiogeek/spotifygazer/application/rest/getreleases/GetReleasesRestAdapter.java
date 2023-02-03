package org.codebusters.audiogeek.spotifygazer.application.rest.getreleases;

import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.spotifygazer.application.rest.getreleases.exception.GetReleasesApiException;
import org.codebusters.audiogeek.spotifygazer.application.rest.util.ErrorResponse;
import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.GetAlbumsByGenreFilter;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.Album;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.Artist;
import org.codebusters.audiogeek.spotifygazer.infrastructure.dataexchange.DatabaseDataQueryAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.codebusters.audiogeek.spotifygazer.application.rest.exception.GlobalApiErrorData.MESSAGE_NOT_READABLE;
import static org.codebusters.audiogeek.spotifygazer.application.rest.getreleases.exception.GetReleasesApiErrorData.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/v1")
@Slf4j
class GetReleasesRestAdapter {

    private static final String valuePrefix = "${gazer.app.get-releases";

    @Value(valuePrefix + ".max-page-size}")
    private int maxPageSize;
    @Value(valuePrefix + ".max-genre-length}")
    private int maxGenreLength;
    @Value(valuePrefix + ".max-genres-size}")
    private int maxGenresSize;
    @Value(valuePrefix + ".char-blacklist}")
    private Set<String> charBlacklist;
    @Value(valuePrefix + ".char-whitelist}")
    private Set<String> charWhitelist;

    @Autowired
    private DatabaseDataQueryAdapter query;

    @PostMapping(value = "/get-current-releases", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<GetReleasesResponse> getCurrentReleases(@RequestBody GetReleasesRequest request,
                                                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                                                  @RequestParam(name = "size", defaultValue = "${gazer.app.max-page-size}") int size) {
        log.info("Processing POST request /get-current-releases: request={}, page={}, size={}", request, page, size);
        checkPageable(page, size);
        checkGenres(request.genres());
        var albumsPage = query.getAlbums(new GetAlbumsByGenreFilter(request.genres(), PageRequest.of(page, size)));
        var total = albumsPage.getTotalElements();
        var albums = albumsPage.stream().map(this::convertToOutsideSchema).collect(toSet());
        var response = GetReleasesResponse.builder()
                .albums(albums)
                .page(page)
                .size(size)
                .total(total)
                .build();
        log.debug("Successfully processed request: response={}", response);
        return ok(response);
    }

    private void checkPageable(int page, int size) {
        log.trace("Approving given pageable: page={} size={}", page, size);
        checkPageSize(size);
        checkPageIndex(page);
        log.trace("Pageable is fully correct");
    }

    private void checkGenres(Set<String> genres) {
        log.trace("Approving given genres: genres={}", genres);
        checkIfGenresNull(genres);
        checkGenresSize(genres.size());
        genres.forEach(this::checkGenre);
        log.trace("Genres are fully approved");
    }

    private void checkGenre(String genre) {
        if (genre.length() > maxGenreLength) {
            log.warn("Given genre is too long: genre={}", genre);
            throw new GetReleasesApiException(GENRE_TOO_LONG, genre, maxGenreLength);
        } else if (genre.length() == 0) {
            log.warn("Given genre is empty");
            throw new GetReleasesApiException(GENRE_EMPTY);
        }
        checkIfGenreContainsIllegalSymbol(genre);
    }

    private void checkIfGenresNull(Set<String> genres) {
        if (genres == null) {
            log.warn("Genres are null");
            throw new GetReleasesApiException(GENRES_NULL);
        }
    }

    private void checkGenresSize(int size) {
        if (size == 0) {
            log.warn("Genres are empty");
            throw new GetReleasesApiException(GENRES_EMPTY);
        } else if (size > maxGenresSize) {
            log.warn("There is too many genres: size={}", size);
            throw new GetReleasesApiException(GENRES_TOO_LONG, maxGenresSize);
        }
    }


    private void checkIfGenreContainsIllegalSymbol(String genre) {
        List<Character> chars = genre.chars().mapToObj(e -> (char) e).toList();
        List<String> symbols = null;
        if (charBlacklist.size() > 0 && charBlacklist.stream().anyMatch(genre::contains)) {
            symbols = charBlacklist.stream().filter(genre::contains).toList();
        } else if (charWhitelist.size() > 0 && chars.stream().map(Object::toString).anyMatch(c -> !charWhitelist.contains(c))) {
            symbols = chars.stream()
                    .map(Object::toString)
                    .filter(c -> !charWhitelist.contains(c)).toList();
        }
        if (symbols != null) {
            log.error("Genre contains illegal symbol(s): genre={}, symbols={}", genre, symbols);
            throw new GetReleasesApiException(GENRES_ILLEGAL_CHARACTER, genre, symbols.toString());
        }
    }

    private GetReleasesAlbum convertToOutsideSchema(Album album) {
        var artists = album.artists().stream()
                .map(Artist::name)
                .collect(toSet());
        return GetReleasesAlbum.builder()
                .title(album.title())
                .artists(artists)
                .genres(album.genres())
                .releaseDate(album.releaseDate().toString())
                .link(album.link())
                .build();
    }

    private static void checkPageIndex(int index) {
        if (index < 0) {
            log.warn("Given page index is wrong: index={}", index);
            throw new GetReleasesApiException(WRONG_PAGE_INDEX);
        }
    }

    private void checkPageSize(int size) {
        if (size > maxPageSize) {
            log.warn("Given page size is too big");
            throw new GetReleasesApiException(PAGE_SIZE_EXCEEDED, maxPageSize);
        } else if (size <= 0) {
            log.warn("Given page size is wrong: size={}", size);
            throw new GetReleasesApiException(WRONG_PAGE_SIZE);
        }
    }

    @ExceptionHandler(GetReleasesApiException.class)
    private ResponseEntity<ErrorResponse> apiExceptionHandler(GetReleasesApiException err) {
        return status(err.getHttpStatus())
                .body(new ErrorResponse(err.getCode(), err.getMessage()));
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    private ResponseEntity<ErrorResponse> httpMessageNotReadable(HttpMediaTypeNotAcceptableException err) {
        log.error("!!!!!!!!!!!");
        return status(MESSAGE_NOT_READABLE.getHttpStatus())
                .body(new ErrorResponse(MESSAGE_NOT_READABLE.getCode(), MESSAGE_NOT_READABLE.getMessage()));
    }
}