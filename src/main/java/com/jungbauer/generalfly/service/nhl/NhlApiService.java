package com.jungbauer.generalfly.service.nhl;

import com.jungbauer.generalfly.dto.nhl.api.Standings;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NhlApiService {

    private final RestClient restClient;

    public NhlApiService(RestClient.Builder restClientBuilder) {
        String API_BASE_URL = "https://api-web.nhle.com/v1";
        this.restClient = restClientBuilder.baseUrl(API_BASE_URL).build();
    }

    public Standings getStandingsNow() {
        return restClient.get().uri("/standings/now").retrieve().body(Standings.class);
    }

}
