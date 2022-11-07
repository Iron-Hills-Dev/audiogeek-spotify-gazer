package org.codebusters.audiogeek.spotifygazer.infrastructure.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "artist")
public class ArtistEntity {
    @Id
    @Getter
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Getter
    @Setter
    @Column(name = "service_id", length = 100)
    private String serviceId;

    @Getter
    @Setter
    @Column(name = "name", length = 100)
    private String name;

    public ArtistEntity(String serviceId, String name) {
        this.serviceId = serviceId;
        this.name = name;
    }
}
