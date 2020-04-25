package org.aforgues.tock.domain;

import org.aforgues.tock.domain.Game;
import org.aforgues.tock.domain.GameType;
import org.aforgues.tock.domain.Player;
import org.aforgues.tock.domain.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class PlayerTest {

    Player player;

    @Mock
    Team team;

    @Mock
    Game ongoingGame;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getOverallRank_shouldReturn1WhenType4PWithTeam1PlayerRank1() {
        // Given
        player = initPlayerAndTeamAndOngoingGame(GameType.FOUR_PLAYERS, 1, 1);
        // When
        int actualResult = player.getOverallRank();

        // Then
        Assertions.assertEquals(1, actualResult);
    }

    @Test
    void getOverallRank_shouldReturn2WhenType4PWithTeam2PlayerRank1() {
        // Given
        player = initPlayerAndTeamAndOngoingGame(GameType.FOUR_PLAYERS, 2, 1);

        // When
        int actualResult = player.getOverallRank();

        // Then
        Assertions.assertEquals(2, actualResult);
    }

    @Test
    void getOverallRank_shouldReturn3WhenType4PWithTeam1PlayerRank2() {
        // Given
        player = initPlayerAndTeamAndOngoingGame(GameType.FOUR_PLAYERS, 1, 2);

        // When
        int actualResult = player.getOverallRank();

        // Then
        Assertions.assertEquals(3, actualResult);
    }

    @Test
    void getOverallRank_shouldReturn4WhenType4PWithTeam2PlayerRank2() {
        // Given
        player = initPlayerAndTeamAndOngoingGame(GameType.FOUR_PLAYERS, 2, 2);

        // When
        int actualResult = player.getOverallRank();

        // Then
        Assertions.assertEquals(4, actualResult);
    }

    @Test
    void getOverallRank_shouldReturn1WhenType6PWithTeam1PlayerRank1() {
        // Given
        player = initPlayerAndTeamAndOngoingGame(GameType.SIX_PLAYERS, 1, 1);

        // When
        int actualResult = player.getOverallRank();

        // Then
        Assertions.assertEquals(1, actualResult);
    }

    @Test
    void getOverallRank_shouldReturn2WhenType6PWithTeam2PlayerRank1() {
        // Given
        player = initPlayerAndTeamAndOngoingGame(GameType.SIX_PLAYERS, 2, 1);

        // When
        int actualResult = player.getOverallRank();

        // Then
        Assertions.assertEquals(2, actualResult);
    }

    @Test
    void getOverallRank_shouldReturn3WhenType6PWithTeam3PlayerRank1() {
        // Given
        player = initPlayerAndTeamAndOngoingGame(GameType.SIX_PLAYERS, 3, 1);

        // When
        int actualResult = player.getOverallRank();

        // Then
        Assertions.assertEquals(3, actualResult);
    }

    @Test
    void getOverallRank_shouldReturn4WhenType6PWithTeam1PlayerRank2() {
        // Given
        player = initPlayerAndTeamAndOngoingGame(GameType.SIX_PLAYERS, 1, 2);

        // When
        int actualResult = player.getOverallRank();

        // Then
        Assertions.assertEquals(4, actualResult);
    }

    @Test
    void getOverallRank_shouldReturn5WhenType6PWithTeam2PlayerRank2() {
        // Given
        player = initPlayerAndTeamAndOngoingGame(GameType.SIX_PLAYERS, 2, 2);

        // When
        int actualResult = player.getOverallRank();

        // Then
        Assertions.assertEquals(5, actualResult);
    }

    @Test
    void getOverallRank_shouldReturn6WhenType6PWithTeam3PlayerRank2() {
        // Given
        player = initPlayerAndTeamAndOngoingGame(GameType.SIX_PLAYERS, 3, 2);

        // When
        int actualResult = player.getOverallRank();

        // Then
        Assertions.assertEquals(6, actualResult);
    }

    @Test
    void getOverallRank_shouldThrowExceptionWhenTypeUnknown() {
        // Given
        player = initPlayerAndTeamAndOngoingGame(GameType.UNKNOWN, 1, 1);

        // When
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> player.getOverallRank());

        // Then
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Unknown game type while computing player overallRank " + GameType.UNKNOWN, exception.getMessage());
    }

    private Player initPlayerAndTeamAndOngoingGame(GameType gameType, int teamRank, int playerRank) {
        Player newPlayer = new Player(team, null, playerRank);
        Mockito.when(team.getRank()).thenReturn(teamRank);
        Mockito.when(team.getOngoingGame()).thenReturn(ongoingGame);
        Mockito.when(ongoingGame.getType()).thenReturn(gameType);
        return newPlayer;
    }
}