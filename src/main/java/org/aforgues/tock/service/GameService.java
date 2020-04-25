package org.aforgues.tock.service;

import org.aforgues.tock.domain.Game;
import org.aforgues.tock.domain.GameType;
import org.aforgues.tock.domain.Player;
import org.aforgues.tock.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public String createGame() {
        Game game = new Game(GameType.FOUR_PLAYERS);
        return gameRepository.save(game);
    }

    public Map<String, Game> mapByKey() {
        return gameRepository.mapByKey();
    }

    public Game findByKey(String id) {
        return gameRepository.findByKey(id);
    }

    public Game currentPlayerStart(String id) {
        Game game = findByKey(id);
        game.getCurrentPlayer().start();
        game.nextPlayer();
        return game;
    }

    // TODO : remove and keep only currentPlayerStart method
    public Game playerStart(String id, int playerOverallRank) {
        Game game = findByKey(id);
        Player player = game.getPlayerByOverallRank(playerOverallRank);
        player.start();
        return game;
    }

    public Game moveCurrentPlayerPawnTo(String gameId, int pawnNumber, int moveCount) {
        Game game = findByKey(gameId);
        game.getCurrentPlayer().movePawnTo(pawnNumber, moveCount);
        game.nextPlayer();
        return game;
    }

    // TODO : remove and keep only moveCurrentPlayerPawnTo method
    public Game movePlayerPawnTo(String gameId, int overallRank, int pawnNumber, int moveCount) {
        Game game = findByKey(gameId);
        Player player = game.getPlayerByOverallRank(overallRank);
        player.movePawnTo(pawnNumber, moveCount);
        return game;
    }

    public Game switchCurrentPlayerPawnWith(String gameId, int pawnNumber, int targetPawnPosition) {
        Game game = findByKey(gameId);
        game.getCurrentPlayer().switchPawns(pawnNumber, targetPawnPosition);
        game.nextPlayer();
        return game;
    }

    // TODO : remove and keep only switchCurrentPlayerPawnWith method
    public Game switchPlayerPawnWith(String gameId, int overallRank, int pawnNumber, int targetPawnPosition) {
        Game game = findByKey(gameId);
        Player player = game.getPlayerByOverallRank(overallRank);
        player.switchPawns(pawnNumber, targetPawnPosition);
        return game;
    }
}
