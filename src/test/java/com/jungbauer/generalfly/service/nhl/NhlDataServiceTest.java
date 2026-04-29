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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private List<Season> seasons;

    @BeforeEach
    void setUp() throws IOException {
        nhlDataService = new NhlDataService(nhlApiService, teamRepository, gameRepository,
                conferenceRepository, divisionRepository, seasonRepository);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        seasons = loadSeasonsFromJson();
    }

    @Test
    void getSeasonForDate_withDateDuringSeason_returnsCurrentSeason() {
        LocalDate duringSeason = LocalDate.of(2025, 1, 15);
        when(seasonRepository.findAll(any(Sort.class))).thenReturn(seasons);

        String result = nhlDataService.getSeasonForDate(duringSeason);

        assertEquals("20242025", result);
    }

    @Test
    void getSeasonForDate_withDateBeforePreseason_returnsOlderSeason() {
        LocalDate beforePreseason = LocalDate.of(2024, 9, 1);
        when(seasonRepository.findAll(any(Sort.class))).thenReturn(seasons);

        String result = nhlDataService.getSeasonForDate(beforePreseason);

        assertEquals("20232024", result);
    }

    @Test
    void getSeasonForDate_withDateOnPreseasonStart_returnsCorrectSeason() {
        LocalDate preseasonStart = LocalDate.of(2024, 9, 21);
        when(seasonRepository.findAll(any(Sort.class))).thenReturn(seasons);

        String result = nhlDataService.getSeasonForDate(preseasonStart);

        assertEquals("20242025", result);
    }

    @Test
    void getSeasonForDate_seasonWithNoPreseason_returnsCorrectSeason() {
        LocalDate preseasonStart = LocalDate.of(2022, 10, 21);
        when(seasonRepository.findAll(any(Sort.class))).thenReturn(seasons);

        String result = nhlDataService.getSeasonForDate(preseasonStart);

        assertEquals("20222023", result);
    }

    @Test
    void getSeasonForDate_withDateOutsideOldestSeason_returnsCorrectSeason() {
        LocalDate preseasonStart = LocalDate.of(2019, 10, 21);
        when(seasonRepository.findAll(any(Sort.class))).thenReturn(seasons);

        String result = nhlDataService.getSeasonForDate(preseasonStart);

        assertEquals("20202021", result);
    }

    @Test
    void getSeasonForDate_withEmptySeason_throwsCorrectError() {
        LocalDate testDate = LocalDate.of(2019, 10, 21);
        List<Season> emptySeason = new ArrayList<>();
        when(seasonRepository.findAll(any(Sort.class))).thenReturn(emptySeason);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            nhlDataService.getSeasonForDate(testDate);
        });

        assertEquals("Seasons list must not be null or empty", exception.getMessage());
    }

    @Test
    void getSeasonForDate_withNullSeason_throwsCorrectError() {
        LocalDate testDate = LocalDate.of(2019, 10, 21);
        when(seasonRepository.findAll(any(Sort.class))).thenReturn(null);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            nhlDataService.getSeasonForDate(testDate);
        });

        assertEquals("Seasons list must not be null or empty", exception.getMessage());
    }

    @Test
    void getSeasonForDate_withNullDate_throwsCorrectError() {
        when(seasonRepository.findAll(any(Sort.class))).thenReturn(seasons);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            nhlDataService.getSeasonForDate(null);
        });

        assertEquals("TestDate must not be null", exception.getMessage());
    }

    @Test
    void getSeasonForDate_testLoop() {
        LocalDate preseasonStart = LocalDate.of(2022, 9, 21);
        when(seasonRepository.findAll(any(Sort.class))).thenReturn(seasons);

        String result = nhlDataService.getSeasonForDate(preseasonStart);

        assertEquals("20212022", result);
    }

    @Test
    void getCurrentSeason_noArgs_returnsSeasonBasedOnCurrentDate() throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        when(seasonRepository.findAll(any(Sort.class))).thenReturn(seasons);

        Method method = NhlDataService.class.getDeclaredMethod("getCurrentSeason");
        method.setAccessible(true);
        String result = (String) method.invoke(nhlDataService);

        assert result.matches("^\\d{8}$") : "Result should be an 8-digit season string";
    }

    private List<Season> loadSeasonsFromJson() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/nhl/seasons.json");
        return objectMapper.readValue(inputStream, new TypeReference<List<Season>>() {});
    }
}
