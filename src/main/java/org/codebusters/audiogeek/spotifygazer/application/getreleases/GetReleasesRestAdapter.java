package org.codebusters.audiogeek.spotifygazer.application.getreleases;

import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.spotifygazer.application.getreleases.exceptions.PageSizeExceededException;
import org.codebusters.audiogeek.spotifygazer.application.util.ErrorResponse;
import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.GetAlbumsByGenreFilter;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.Album;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.Artist;
import org.codebusters.audiogeek.spotifygazer.infrastructure.dataexchange.DatabaseDataQueryAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import static java.util.stream.Collectors.toSet;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1")
@Slf4j
class GetReleasesRestAdapter {

    @Value("${gazer.app.max-page-size}")
    private int maxSize;

    @Autowired
    private DatabaseDataQueryAdapter query;

    @PostMapping(value = "/get-current-releases", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    GetReleasesResponse getCurrentReleases(@RequestBody GetReleasesRequest request,
                                           @RequestParam Integer page,
                                           @RequestParam Integer size) {
        log.info("Processing POST request /get-current-releases: request={}, page={}, size={}", request, page, size);
        checkSize(size);
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
        return response;
    }

    private void checkSize(int size) {
        if (size > maxSize) {
            throw new PageSizeExceededException("Given page size is too big: given=%s max=%s".formatted(size, maxSize));
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
                .releaseDate(album.releaseDate())
                .link(album.link())
                .build();
    }

    @ExceptionHandler({PageSizeExceededException.class})
    @ResponseStatus(value = BAD_REQUEST)
    private ErrorResponse maxSizeExceededHandler(RuntimeException err) {
        return new ErrorResponse(PageSizeExceededException.CODE, err.getMessage());
    }
}
