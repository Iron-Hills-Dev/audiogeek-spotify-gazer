package org.codebusters.audiogeek.spotifygazer.domain.newreleases.model;

import java.util.Set;


/**
 * Final new releases object.
 * @param albums All new releases (Album set).
 */
public record NewReleases(Set<Album> albums) {
}
