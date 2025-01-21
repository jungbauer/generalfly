package com.jungbauer.generalfly.dto.comics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComicDto {
    @JsonIgnore
    public Integer id;

    public String title;
    public String linkMain;
    public String linkAlternate;
    public Boolean useAlternateLink;
    public Float chapterTotal;
    public Float chapterCurrent;
    public String notes;
}
