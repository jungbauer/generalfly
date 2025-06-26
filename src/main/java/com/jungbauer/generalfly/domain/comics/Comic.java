package com.jungbauer.generalfly.domain.comics;

import com.jungbauer.generalfly.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(schema = "comics")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Comic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PROTECTED)
    private Integer id;

    private String title;
    private String linkMain;
    private String linkAlternate;
    private Boolean useAlternateLink;
    private Float chapterTotal;
    private Float chapterCurrent;

    @Column(columnDefinition = "text")
    private String notes;

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant updatedOn;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    public void incrementChapterCurrent() {
        chapterCurrent += 1.0f;
        if (chapterCurrent > chapterTotal) {
            chapterTotal = chapterCurrent;
        }
    }
}
