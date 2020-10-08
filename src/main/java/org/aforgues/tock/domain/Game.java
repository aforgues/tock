package org.aforgues.tock.domain;

import lombok.Data;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Game {
    @Getter
    @Setter
    private String gameId;

    @Getter
    private GameType type;

    @Getter
    private Player currentPlayer;

    @Getter
    private Set<Team> teams;

    @Getter
    private GameBoard gameBoard;

    private CardDeck cardDeck;

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

        this.cardDeck = new CardDeck();
        this.cardDeck.distributeToPlayers(this.getAllPlayers());
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

    private List<Player> getAllPlayers() {
        return this.teams.stream()
                .map(team -> team.getPlayers())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
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

    // TODO : move in a card service ?
    public List<Card> getCurrentPlayerCardHand() {
        return this.cardDeck.getPlayerCardHand(this.currentPlayer);
    }

    public Card getCardById(String cardId) {
        return this.getCurrentPlayerCardHand().stream()
                .filter(card -> card.getCardId().equals(cardId))
                .findFirst()
                .orElse(null);
    }

    public void transferCardToDiscardPile(Card card, Player currentPlayer) {
        this.cardDeck.moveCardFromPlayerToDiscardPile(card, currentPlayer);
    }

    public List<Card> getDiscardPile() {
        return this.cardDeck.getDiscardPile();
    }
}
