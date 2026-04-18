package com.jungbauer.generalfly.service.nhl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jungbauer.generalfly.domain.nhl.Season;
import com.jungbauer.generalfly.repository.nhl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NhlDataServiceTest {

    @Mock
    private NhlApiService nhlApiService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private ConferenceRepository conferenceRepository;

    @Mock
    private DivisionRepository divisionRepository;

    @Mock
    private SeasonRepository seasonRepository;

    private NhlDataService nhlDataService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        nhlDataService = new NhlDataService(nhlApiService, teamRepository, gameRepository,
                conferenceRepository, divisionRepository, seasonRepository);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getCurrentSeasonStr_withDateDuringSeason_returnsCurrentSeason() throws IOException {
        List<Season> seasons = loadSeasonsFromJson();
        LocalDate duringSeason = LocalDate.of(2025, 1, 15);

        String result = nhlDataService.getCurrentSeasonStr(duringSeason, seasons);

        assertEquals("20242025", result);
    }

    @Test
    void getCurrentSeasonStr_withDateBeforePreseason_returnsOlderSeason() throws IOException {
        List<Season> seasons = loadSeasonsFromJson();
        LocalDate beforePreseason = LocalDate.of(2024, 9, 1);

        String result = nhlDataService.getCurrentSeasonStr(beforePreseason, seasons);

        assertEquals("20232024", result);
    }

    @Test
    void getCurrentSeasonStr_withDateOnPreseasonStart_returnsCorrectSeason() throws IOException {
        List<Season> seasons = loadSeasonsFromJson();
        LocalDate preseasonStart = LocalDate.of(2024, 9, 21);

        String result = nhlDataService.getCurrentSeasonStr(preseasonStart, seasons);

        assertEquals("20242025", result);
    }

    @Test
    void getCurrentSeasonStr_seasonWithNoPreseason_returnsCorrectSeason() throws IOException {
        List<Season> seasons = loadSeasonsFromJson();
        LocalDate preseasonStart = LocalDate.of(2022, 10, 21);

        String result = nhlDataService.getCurrentSeasonStr(preseasonStart, seasons);

        assertEquals("20222023", result);
    }

    @Test
    void getCurrentSeasonStr_withDateOutsideOldestSeason_returnsCorrectSeason() throws IOException {
        List<Season> seasons = loadSeasonsFromJson();
        LocalDate preseasonStart = LocalDate.of(2019, 10, 21);

        String result = nhlDataService.getCurrentSeasonStr(preseasonStart, seasons);

        assertEquals("20212022", result);
    }

    @Test
    void getCurrentSeasonStr_testLoop() throws IOException {
        List<Season> seasons = loadSeasonsFromJson();
        LocalDate preseasonStart = LocalDate.of(2022, 9, 21);

        String result = nhlDataService.getCurrentSeasonStr(preseasonStart, seasons);

        assertEquals("20212022", result);
    }

    @Test
    void getCurrentSeasonStr_noArgs_returnsSeasonBasedOnCurrentDate() throws IOException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        List<Season> seasons = loadSeasonsFromJson();
        when(seasonRepository.findAll(any(Sort.class))).thenReturn(seasons);

        Method method = NhlDataService.class.getDeclaredMethod("getCurrentSeasonStr");
        method.setAccessible(true);
        String result = (String) method.invoke(nhlDataService);

        assert result.matches("^\\d{8}$") : "Result should be an 8-digit season string";
    }

    private List<Season> loadSeasonsFromJson() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/nhl/seasons.json");
        return objectMapper.readValue(inputStream, new TypeReference<List<Season>>() {});
    }
}
