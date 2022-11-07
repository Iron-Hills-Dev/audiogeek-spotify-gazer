package org.codebusters.audiogeek.spotifygazer.infrastructure.db;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.EAGER;

@Entity
@Builder
@Table(name = "album")
public class AlbumEntity {
    @Id
    @GeneratedValue
    @Getter
    @Column(name = "id")
    private UUID id;

    @Getter
    @Setter
    @Column(name = "provider_id")
    private String providerId;

    @Getter
    @Setter
    @Column(name = "title")
    private String title;

    @Getter
    @Setter
    @Column(name = "release_date")
    private LocalDate releaseDate;

    @ManyToMany(cascade = PERSIST, fetch = EAGER)
    @JoinTable(
            name = "album_genre",
            joinColumns = @JoinColumn(name = "album_id", foreignKey = @ForeignKey(name = "ag_albumId_fk")),
            inverseJoinColumns = @JoinColumn(name = "genre_id", foreignKey = @ForeignKey(name = "ag_genreId_fk")))
    private Set<GenreEntity> genres;

    @ManyToMany(cascade = PERSIST, fetch = EAGER)
    @JoinTable(
            name = "album_artist",
            joinColumns = @JoinColumn(name = "album_id", foreignKey = @ForeignKey(name = "aa_albumId_fk")),
            inverseJoinColumns = @JoinColumn(name = "artist_id", foreignKey = @ForeignKey(name = "aa_artistId_fk")))
    private Set<ArtistEntity> artists;
}
