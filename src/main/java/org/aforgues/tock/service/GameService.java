package org.aforgues.tock.service;

import org.aforgues.tock.domain.Card;
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

    public Game playerStart(String id, int playerOverallRank) {
        Game game = findByKey(id);
        Player player = game.getPlayerByOverallRank(playerOverallRank);
        player.start();
        return game;
    }

    public Game movePlayerPawnTo(String gameId, int overallRank, int pawnNumber, int moveCount) {
        Game game = findByKey(gameId);
        Player player = game.getPlayerByOverallRank(overallRank);
        player.movePawnTo(pawnNumber, moveCount);
        return game;
    }

    public Game switchPlayerPawnWith(String gameId, int overallRank, int pawnNumber, int targetPawnPosition) {
        Game game = findByKey(gameId);
        Player player = game.getPlayerByOverallRank(overallRank);
        player.switchPawns(pawnNumber, targetPawnPosition);
        return game;
    }

    public Game playCurrentPlayer(String gameId, Card card, Integer pawnNumber, Integer targetPosition) {
        Game game = findByKey(gameId);
        Player currentPlayer = game.getCurrentPlayer();

        if (card.isCanSwitch()) {
            currentPlayer.switchPawns(pawnNumber, targetPosition);
        }
        else if (card.isCanStart() &&
                currentPlayer.getPawn(pawnNumber).isAtHome()) {
            currentPlayer.start(pawnNumber);
        }
        else {
            // TODO : check targetPosition match with card moveCount
            currentPlayer.movePawnTo(pawnNumber, card.getMoveCount());
        }
        // TODO : add canSplit behavior

        game.nextPlayer();
        return game;
    }
}
