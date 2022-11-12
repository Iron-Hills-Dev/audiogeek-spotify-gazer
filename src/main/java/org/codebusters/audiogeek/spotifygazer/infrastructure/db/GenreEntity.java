package org.codebusters.audiogeek.spotifygazer.infrastructure.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(
        name = "genre",
        uniqueConstraints = {@UniqueConstraint(name = "g_name_uk", columnNames = "name")}
)
public class GenreEntity {
    @Id
    @Getter
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Getter
    @Setter
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    public GenreEntity(String name) {
        this.name = name;
    }
}
