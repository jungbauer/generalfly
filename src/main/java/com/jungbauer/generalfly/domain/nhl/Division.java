package com.jungbauer.generalfly.domain.nhl;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(schema = "nhl")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Division {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PROTECTED)
    private Integer id;

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant updatedOn;

    private String abbrev;
    private String name;

    public Division(String abbrev, String name) {
        this.abbrev = abbrev;
        this.name = name;
    }
}
