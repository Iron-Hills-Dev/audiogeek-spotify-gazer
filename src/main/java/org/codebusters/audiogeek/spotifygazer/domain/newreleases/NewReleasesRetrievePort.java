package org.codebusters.audiogeek.spotifygazer.domain.newreleases;

import org.codebusters.audiogeek.spotifygazer.domain.newreleases.model.NewReleases;

import java.util.Optional;

/**
 * Interface for getting new releases.
 */
public interface NewReleasesRetrievePort {
    /**
     * Gets new music releases.
     *
     * @return list of new albums with their data.
     */
    Optional<NewReleases> getNewReleases();
}
