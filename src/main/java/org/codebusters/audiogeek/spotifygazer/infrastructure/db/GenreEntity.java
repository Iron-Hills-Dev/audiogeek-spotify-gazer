package org.codebusters.audiogeek.spotifygazer.infrastructure.db;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Table(
        name = "genre",
        uniqueConstraints = {@UniqueConstraint(name = "g_name_uk", columnNames = "name")}
)
public class GenreEntity {
    @Id
    @Getter
    @GeneratedValue
    @Column(name = "id")
    @EqualsAndHashCode.Exclude
    private UUID id;

    @Getter
    @Setter
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    public GenreEntity(String name) {
        this.name = name;
    }
}
