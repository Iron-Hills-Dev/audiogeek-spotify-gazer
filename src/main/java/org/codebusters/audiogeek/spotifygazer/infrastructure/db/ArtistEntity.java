package org.codebusters.audiogeek.spotifygazer.infrastructure.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(
        name = "artist",
        uniqueConstraints = {@UniqueConstraint(name = "ar_name_providerId_uk", columnNames = {"name", "provider_id"})}
)
public class ArtistEntity {
    @Id
    @Getter
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Getter
    @Setter
    @Column(name = "provider_id", length = 100)
    private String providerId;

    @Getter
    @Setter
    @Column(name = "name")
    private String name;

    public ArtistEntity(String providerId, String name) {
        this.providerId = providerId;
        this.name = name;
    }
}
