package org.codebusters.audiogeek.spotifygazer.infrastructure.dataexchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.DataQueryPort;
import org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model.GetAlbumsByGenreFilter;
import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.Album;
import org.codebusters.audiogeek.spotifygazer.infrastructure.db.repo.AlbumRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.codebusters.audiogeek.spotifygazer.infrastructure.dataexchange.util.EntityUtils.convertToAlbum;

@RequiredArgsConstructor
@Slf4j
public class DatabaseDataQueryAdapter implements DataQueryPort {

    private final AlbumRepository albumRepo;

    @Override
    public Page<Album> getAlbums(Pageable pageable) {
        log.debug("Processing get albums query: pageable={}", pageable);
        var page = albumRepo.findAllOrderByReleaseDate(pageable);
        var albums = page.stream()
                .map(a -> convertToAlbum(a, a.getArtists(), a.getGenres()))
                .toList();
        return new PageImpl<>(albums, pageable, page.getTotalElements());
    }

    @Override
    public Page<Album> getAlbums(GetAlbumsByGenreFilter filter) {
        log.debug("Processing get albums query: filter={}", filter);
        var page = albumRepo.findByGenres(filter.genres(), filter.pageable());
        var albums = page.stream()
                .map(a -> convertToAlbum(a, a.getArtists(), a.getGenres()))
                .toList();
        return new PageImpl<>(albums, filter.pageable(), page.getTotalElements());
    }
}
