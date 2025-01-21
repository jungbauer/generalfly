package com.jungbauer.generalfly.dto.comics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComicDto {
    public Integer id;
    public String title;
    public String linkMain;
    public String linkAlternate;
    public Boolean useAlternateLink;
    public Float chapterTotal;
    public Float chapterCurrent;
    public String notes;
}
