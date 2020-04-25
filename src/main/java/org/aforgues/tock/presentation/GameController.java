package org.aforgues.tock.presentation;

import lombok.extern.slf4j.Slf4j;
import org.aforgues.tock.domain.*;
import org.aforgues.tock.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@Slf4j
public class GameController {
    @Autowired
    private GameService gameService;

    @GetMapping("/games/new")
    public String newGame() {
        String id = gameService.createGame();
        return "redirect:/games/" + id;
    }

    // TODO : create Listing HTML page instead
    @GetMapping("/games")
    public ResponseEntity<Set<String>> listGames() {
        Map<String, Game> games = gameService.mapByKey();
        return new ResponseEntity<>(games.keySet(), HttpStatus.OK);
    }

    @GetMapping("/games/{id}")
    public String details(@PathVariable String id, Model model) {
        Game game = gameService.findByKey(id);
        buildModelAndReturnTemplateFourPlayer(model, game);
        return "Tock4";
    }

    @GetMapping("/games/{id}/players/current/start")
    public String currentPlayerStart(@PathVariable String id, Model model) {
        Game game = gameService.currentPlayerStart(id);
        buildModelAndReturnTemplateFourPlayer(model, game);
        return "Tock4";
    }

    @GetMapping("/games/{id}/players/{overallRank}/start")
    public String playerStart(@PathVariable String id, @PathVariable int overallRank, Model model) {
        Game game = gameService.playerStart(id, overallRank);
        buildModelAndReturnTemplateFourPlayer(model, game);
        return "Tock4";
    }

    @GetMapping("/games/{gameId}/players/current/pawns/{pawnNumber}/move/{moveCount}")
    public String moveCurrentPlayerPawnTo(@PathVariable String gameId, @PathVariable int pawnNumber, @PathVariable int moveCount, Model model) {
        Game game = gameService.moveCurrentPlayerPawnTo(gameId, pawnNumber, moveCount);
        buildModelAndReturnTemplateFourPlayer(model, game);
        return "Tock4";
    }

    @GetMapping("/games/{gameId}/players/{overallRank}/pawns/{pawnNumber}/move/{moveCount}")
    public String movePlayerPawnTo(@PathVariable String gameId, @PathVariable int overallRank,
                                   @PathVariable int pawnNumber, @PathVariable int moveCount, Model model) {
        Game game = gameService.movePlayerPawnTo(gameId, overallRank, pawnNumber, moveCount);
        buildModelAndReturnTemplateFourPlayer(model, game);
        return "Tock4";
    }

    @GetMapping("/games/{gameId}/players/current/pawns/{pawnNumber}/switch/{targetPawnPosition}")
    public String switchPlayerPawnWith(@PathVariable String gameId, @PathVariable int pawnNumber, @PathVariable int targetPawnPosition, Model model) {
        Game game = gameService.switchCurrentPlayerPawnWith(gameId, pawnNumber, targetPawnPosition);
        buildModelAndReturnTemplateFourPlayer(model, game);
        return "Tock4";
    }

    @GetMapping("/games/{gameId}/players/{overallRank}/pawns/{pawnNumber}/switch/{targetPawnPosition}")
    public String switchPlayerPawnWith(@PathVariable String gameId, @PathVariable int overallRank,
                                   @PathVariable int pawnNumber, @PathVariable int targetPawnPosition, Model model) {
        Game game = gameService.switchPlayerPawnWith(gameId, overallRank, pawnNumber, targetPawnPosition);
        buildModelAndReturnTemplateFourPlayer(model, game);
        return "Tock4";
    }

    private void buildModelAndReturnTemplateFourPlayer(Model model, Game game) {
        if (game != null) {
            model.addAttribute("currentPlayerPawnColor", game.getCurrentPlayer().getPawnsColor());
            model.addAttribute("gameBoardRows", buildViewModel(game));
        }
    }

    private Collection<FourPlayerGameBoardRowViewModel> buildViewModel(Game game) {
        GameBoard gameBoard = game.getGameBoard();

        Collection<FourPlayerGameBoardRowViewModel> rows = new ArrayList<>();
        for (int row = 0; row <= 24; row++) {
            FourPlayerGameBoardRowViewModel fourPlayerGameBoardRowViewModel = new FourPlayerGameBoardRowViewModel();
            rows.add(fourPlayerGameBoardRowViewModel);
            for (int column = 0; column <= 24; column++) {
                FourPlayerGameBoardCellViewModel fourPlayerGameBoardCellViewModel = new FourPlayerGameBoardCellViewModel();
                fourPlayerGameBoardRowViewModel.addCell(fourPlayerGameBoardCellViewModel);

                if (column >= 11 && column <= 13
                        && row >= 11 && row <= 13) {
                    fourPlayerGameBoardCellViewModel.setCenter(true);
                    fourPlayerGameBoardCellViewModel.computeImageCenterNumber(row, column);
                }

                Optional<Integer> position = FourPlayerStandardHoleDisplayMapping.positionFromColumnAndRow(column, row);
                Optional<FourPlayerHomeHoleDisplayMapping.HomeHoleData> positionHome = FourPlayerHomeHoleDisplayMapping.positionFromColumnAndRow(column, row);

                if (position.isPresent()) {
                    fourPlayerGameBoardCellViewModel.setHasHole(true);

                    Hole hole = gameBoard.getHoleByPosition(position.get());
                    if (hole.isFree()) {
                        fourPlayerGameBoardCellViewModel.setHasEmptyHole(true);
                    }
                    else {
                        fourPlayerGameBoardCellViewModel.setHasEmptyHole(false);

                        Pawn pawn = hole.getOccupant();
                        String color = pawn.getOwner().getPawnsColor();

                        fourPlayerGameBoardCellViewModel.setHolePawnColor(color);
                        fourPlayerGameBoardCellViewModel.setHolePawnPlayerNumber(pawn.getPlayerPawnNumber());
                        fourPlayerGameBoardCellViewModel.setPawnStake(hole.hasStakeOccupant());
                        fourPlayerGameBoardCellViewModel.setPawnPlayable(pawn.isPlayable());
                    }
                }
                else if (positionHome.isPresent()) {
                    fourPlayerGameBoardCellViewModel.setHasHole(true);

                    FourPlayerHomeHoleDisplayMapping.HomeHoleData homeHoleData = positionHome.get();
                    Hole homeHole;
                    if (homeHoleData.getHoleType() == HoleType.HOME_START) {
                        homeHole = gameBoard.getStartHomeHoleByPlayerAndPosition(game.getPlayerByOverallRank(homeHoleData.getPlayerRank()),
                                homeHoleData.getHolePosition());
                    }
                    else {
                        homeHole = gameBoard.getFinishHomeHoleByPlayerAndPosition(game.getPlayerByOverallRank(homeHoleData.getPlayerRank()),
                                homeHoleData.getHolePosition());
                    }
                    if (homeHole.isFree()) {
                        fourPlayerGameBoardCellViewModel.setHasEmptyHole(true);
                    }
                    else {
                        fourPlayerGameBoardCellViewModel.setHasEmptyHole(false);

                        Pawn pawn = homeHole.getOccupant();
                        String color = pawn.getOwner().getPawnsColor();

                        fourPlayerGameBoardCellViewModel.setHolePawnColor(color);
                        fourPlayerGameBoardCellViewModel.setHolePawnPlayerNumber(pawn.getPlayerPawnNumber());
                        fourPlayerGameBoardCellViewModel.setPawnPlayable(pawn.isPlayable());
                    }
                }
                else {
                    fourPlayerGameBoardCellViewModel.setHasHole(false);
                }
            }
        }
        return rows;
    }
}