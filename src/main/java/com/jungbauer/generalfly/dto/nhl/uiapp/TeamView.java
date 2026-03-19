package com.jungbauer.generalfly.dto.nhl.uiapp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamView {
    private Integer nhlId;
    private String commonName;
    private String placeName;
    private String abbrev;
    private String logo;
    private String darkLogo;
}
