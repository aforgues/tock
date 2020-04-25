package org.aforgues.tock.domain;

import org.aforgues.tock.domain.Game;
import org.aforgues.tock.domain.GameType;
import org.aforgues.tock.domain.Player;
import org.aforgues.tock.domain.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest {

    Game game4P;
    Game game6P;

    @BeforeEach
    void init() {
        game4P = new Game(GameType.FOUR_PLAYERS);
        game6P = new Game(GameType.SIX_PLAYERS);
    }

    @Test
    void getNextPlayer_Type_4P_TeamB_Player1_After_TeamA_Player1() {
        getNextPlayer(game4P, 1, 1, 2, 1);
    }

    @Test
    void getNextPlayer_Type_4P_TeamAPlayer2_After_TeamBPlayer1() {
        getNextPlayer(game4P, 2, 1, 1, 2);
    }

    @Test
    void getNextPlayer_Type_4P_TeamBPlayer2_After_TeamAPlayer2() {
        getNextPlayer(game4P, 1, 2, 2, 2);
    }

    @Test
    void getNextPlayer_Type_4P_TeamAPlayer1_After_TeamBPlayer2() {
        getNextPlayer(game4P, 2, 2, 1, 1);
    }

    @Test
    void getNextPlayer_Type_6P_TeamBPlayer1_After_TeamAPlayer1() {
        getNextPlayer(game6P, 1, 1, 2, 1);
    }

    @Test
    void getNextPlayer_Type_6P_TeamCPlayer1_After_TeamBPlayer1() {
        getNextPlayer(game6P, 2, 1, 3, 1);
    }

    @Test
    void getNextPlayer_Type_6P_TeamAPlayer2_After_TeamCPlayer1() {
        getNextPlayer(game6P, 3, 1, 1, 2);
    }

    @Test
    void getNextPlayer_Type_6P_TeamBPlayer2_After_TeamAPlayer2() {
        getNextPlayer(game6P, 1, 2, 2, 2);
    }

    @Test
    void getNextPlayer_Type_6P_TeamCPlayer2_After_TeamBPlayer2() {
        getNextPlayer(game6P, 2, 2, 3, 2);
    }

    @Test
    void getNextPlayer_Type_6P_TeamAPlayer1_After_TeamCPlayer2() {
        getNextPlayer(game6P, 3, 2, 1, 1);
    }

    private Player getPlayer (Game game, int teamRank, int playerRank) {
        Team team = game.getTeams().stream().filter(t -> t.getRank() == teamRank).findFirst().get();
        return team.getPlayers().stream().filter(player -> player.getRank() == playerRank).findFirst().get();
    }

    private void getNextPlayer(Game game, int currentTeamRank, int currentPlayerRank, int expectedTeamRank, int expectedPlayerRank) {
        // Given current player
        Player player = getPlayer(game,currentTeamRank, currentPlayerRank);

        // When calling method
        Player nextPlayer = game.getNextPlayer(player);

        // Then next player should be
        Player expectedPlayer = getPlayer(game, expectedTeamRank, expectedPlayerRank);
        Assertions.assertEquals(expectedPlayer, nextPlayer);
    }
}