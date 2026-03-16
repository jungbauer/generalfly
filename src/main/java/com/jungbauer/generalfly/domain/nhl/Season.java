package com.jungbauer.generalfly.domain.nhl;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(schema = "nhl")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PROTECTED)
    private Integer id;

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant updatedOn;

    private Integer nhlId;
    private String formattedSeasonId;
    private Integer seasonOrdinal;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate preseasonStartDate;
    private LocalDate regularSeasonEndDate;
}
