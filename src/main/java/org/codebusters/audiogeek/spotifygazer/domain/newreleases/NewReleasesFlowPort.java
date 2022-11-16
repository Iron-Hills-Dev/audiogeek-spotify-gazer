package org.codebusters.audiogeek.spotifygazer.domain.newreleases;

/**
 * Functional interface for flow surrounding retrieval of new releases
 */
@FunctionalInterface
public interface NewReleasesFlowPort {
    /**
     * Runs flow, which:
     * 1. gets new releases list
     * 2. saves it to storage for future usage
     */
    void run();
}
