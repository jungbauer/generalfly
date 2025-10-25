package com.jungbauer.generalfly.domain.proof;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(schema = "proof")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Target {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PROTECTED)
    private Integer id;

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant updatedOn;

    private String title;

    @Enumerated(EnumType.STRING)
    private CompletionType completionType;

    @Enumerated(EnumType.STRING)
    private TemporalType temporalType;
}
