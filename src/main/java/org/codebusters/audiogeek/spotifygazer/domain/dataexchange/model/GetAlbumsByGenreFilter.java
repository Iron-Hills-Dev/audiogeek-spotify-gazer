package org.codebusters.audiogeek.spotifygazer.domain.dataexchange.model;


import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * Command for searching albums by genre
 *
 * @param genres   Set of genres - album need to have at least one genre from list to be qualified
 * @param pageable Pageable object
 */
public record GetAlbumsByGenreFilter(Set<String> genres,
                                     Pageable pageable) {
}
