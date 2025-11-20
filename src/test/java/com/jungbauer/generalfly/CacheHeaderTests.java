package com.jungbauer.generalfly;

import com.jungbauer.generalfly.dto.nhl.api.ClubSeasonSchedule;
import com.jungbauer.generalfly.dto.nhl.api.GameCenterPlayByPlay;
import com.jungbauer.generalfly.dto.nhl.api.Standings;
import com.jungbauer.generalfly.service.nhl.NhlApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CacheHeaderTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NhlApiService service;

    @Test
    @WithMockUser
    void getStandings_returnsCacheControlWithMaxAge12Hours() throws Exception {
        when(service.getStandingsNow()).thenReturn(new Standings());
        mockMvc.perform(get("/nhl/standings"))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", containsString("max-age=43200")));
    }

    @Test
    @WithMockUser
    void getClubSeasonSchedule_returnsCacheControlWithMaxAge12Hours() throws Exception {
        when(service.getClubSeasonSchedule("TOR", "20252026")).thenReturn(new ClubSeasonSchedule());
        mockMvc.perform(get("/nhl/club-schedule?team=TOR&season=20252026"))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", containsString("max-age=43200")));
    }

    @Test
    @WithMockUser
    void getPlayByPlay_returnsCacheControlWithMaxAge12Hours() throws Exception {
        when(service.getGameCenterPlayByPlay("1")).thenReturn(new GameCenterPlayByPlay());
        mockMvc.perform(get("/nhl/gamecenter-playbyplay?gameId=1"))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", containsString("max-age=43200")));
    }
}
