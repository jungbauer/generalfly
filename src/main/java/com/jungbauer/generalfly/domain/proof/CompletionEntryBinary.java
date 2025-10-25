package com.jungbauer.generalfly.domain.proof;

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
@Table(schema = "proof")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CompletionEntryBinary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PROTECTED)
    private Integer id;

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant updatedOn;

    private Boolean completed;
    private LocalDate completionDate;

    @ManyToOne
    @JoinColumn(name="target_id", nullable = false)
    private Target target;
}
