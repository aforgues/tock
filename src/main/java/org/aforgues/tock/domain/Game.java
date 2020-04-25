package org.aforgues.tock.domain;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
public class Game {
    @Getter
    private GameType type;

    @Getter
    private Player currentPlayer;

    @Getter
    private Set<Team> teams;

    @Getter
    private GameBoard gameBoard;

    public Game(GameType type) {
        this.type = type;
        this.teams = new TreeSet<>();

        switch (type) {
            case FOUR_PLAYERS:
                createFourPlayersGame();
                break;
            case SIX_PLAYERS:
                createSixPlayersGame();
                break;
            default:
                throw new IllegalArgumentException("Unknown Game type " + type);
        }
        this.currentPlayer = firstPlayer();

        this.gameBoard = new GameBoard(teams, this);
    }

    private void createFourPlayersGame() {
        addTeam("Team A", 1);
        addTeam("Team B", 2);
    }

    private void createSixPlayersGame() {
        createFourPlayersGame();
        addTeam("Team C", 3);
    }

    private void addTeam(String teamName, int rank) {
        Team secondTeam = new Team(this, teamName, rank);
        this.teams.add(secondTeam);
    }

    public void nextPlayer() {
        this.currentPlayer = getNextPlayer(this.currentPlayer);
    }

    public Player getNextPlayer(Player currentPlayer) {
        int currentPlayerOverallRank = currentPlayer.getOverallRank();
        int nextOverallRank = (currentPlayerOverallRank + 1) > this.getTeams().size()*2 ? 1 : currentPlayerOverallRank + 1;

        return this.teams.stream()
                .map(team -> team.getPlayers())
                .flatMap(Collection::stream)
                .filter(player -> player.getOverallRank() == nextOverallRank)
                .findFirst()
                .get();
    }

    /**
     * Utility methods for test purpose
     * @return
     */
    public Player currentPlayer() {
        // TODO: manage current player later, for now it is a random player of one of the teams
        int teamRank = new Random().nextInt(this.teams.size()) + 1;
        log.info("Random team rank : " + teamRank);
        int playerRank = new Random().nextInt(2) + 1;
        log.info("Random player rank : " + playerRank);
        return this.teams.stream()
                .filter(team -> team.getRank() == teamRank)
                .findFirst()
                .map(team -> team.getPlayers())
                .stream()
                .flatMap(Collection::stream)
                .filter(player -> player.getRank() == playerRank)
                .findAny()
                .get();
    }

    public Player firstPlayer() {
        return getPlayerByOverallRank(1);
    }

    public Player lastPlayer() {
        return getPlayerByOverallRank(this.teams.size() * 2);
    }

    public Player getPlayerByOverallRank(final int overallRank) {
        return this.teams.stream()
                .map(team -> team.getPlayers())
                .flatMap(Collection::stream)
                .filter(player -> player.getOverallRank() == overallRank)
                .findFirst()
                .get();
    }

    public String toString() {
        return "GameType : " + this.getType() + "\n" +
               "GameBoard : " + this.gameBoard;
    }
}
