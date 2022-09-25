package org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow;

import org.codebusters.audiogeek.spotifygazer.domain.newreleasesflow.model.NewReleases;

import java.util.Optional;

/**
 *
 */
public interface NewReleasesFlowPort {
    /**
     * Gets new music releases.
     *
     * @return list of new albums with their data
     */
    Optional<NewReleases> getNewReleases();
}
