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
@Table(schema = "nhl", name = "games")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PROTECTED)
    private Integer id;

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant updatedOn;

    private Long nhlGameId;
    private Long season;
    private Integer gameType;
    private String gameState;
    private String gameOutcome;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
    private String gameDate;

    @ManyToOne
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;

    @ManyToOne
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;
}
