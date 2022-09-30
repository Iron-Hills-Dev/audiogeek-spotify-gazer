package org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow;

import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model.NewReleases;

import java.util.Optional;

/**
 * Interface for getting new releases.
 */
public interface NewReleasesFlowPort {
    /**
     * Gets new music releases.
     *
     * @return list of new albums with their data.
     */
    Optional<NewReleases> getNewReleases();
}
