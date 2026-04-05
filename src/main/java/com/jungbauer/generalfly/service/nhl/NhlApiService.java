package com.jungbauer.generalfly.service.nhl;

import com.jungbauer.generalfly.dto.nhl.api.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class NhlApiService {

    private final RestClient restClient;
    private final RestClient restClient2;

    public NhlApiService(RestClient.Builder restClientBuilder, RestClient.Builder restClientBuilder2) {
        String API_BASE_URL = "https://api-web.nhle.com/v1";
        this.restClient = restClientBuilder.baseUrl(API_BASE_URL).build();

        String API_BASE_URL_2 = "https://api.nhle.com";
        this.restClient2 = restClientBuilder2.baseUrl(API_BASE_URL_2).build();
    }

    public Standings getStandingsNow() {
        return restClient.get().uri("/standings/now").retrieve().body(Standings.class);
    }

    public ClubSeasonSchedule getClubSeasonSchedule(String teamCode, String season) {
        return restClient.get().uri("/club-schedule-season/{teamCode}/{season}", teamCode, season).retrieve().body(ClubSeasonSchedule.class);
    }

    public GameCenterPlayByPlay getGameCenterPlayByPlay(String gameId) {
        return restClient.get().uri("/gamecenter/{gameId}/play-by-play", gameId).retrieve().body(GameCenterPlayByPlay.class);
    }

    public ScheduleDate getScheduleByDate(String date) {
        return restClient.get().uri("/schedule/{date}", date).retrieve().body(ScheduleDate.class);
    }

    public List<Long> getBasicSeasons() {
        return restClient.get().uri("/season").retrieve().body(new ParameterizedTypeReference<List<Long>>() {});
    }

    public Seasons getDetailedSeasons() {
        return restClient2.get().uri("/stats/rest/en/season").retrieve().body(Seasons.class);
    }

}
