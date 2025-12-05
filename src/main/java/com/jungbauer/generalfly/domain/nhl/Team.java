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
@Table(schema = "nhl", name = "teams")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PROTECTED)
    private Integer id;

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant updatedOn;

    private Integer nhlId;
    private String commonName;
    private String placeName;
    private String abbrev;
    private String logo;
    private String darkLogo;

    @ManyToOne
    @JoinColumn(name = "conference_id")
    private Conference conference;

    @ManyToOne
    @JoinColumn(name = "division_id")
    private Division division;

}
