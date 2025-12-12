package com.jungbauer.generalfly.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "dump_logs")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class DumpLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant updatedOn;

    @Column(columnDefinition = "text")
    private String file;

    @Column(columnDefinition = "text")
    private String method;

    @Column(columnDefinition = "text")
    private String message;

    public DumpLog(String file, String method, String message) {
        this.file = file;
        this.method = method;
        this.message = message;
    }
}
