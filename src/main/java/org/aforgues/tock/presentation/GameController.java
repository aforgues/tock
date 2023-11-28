package org.aforgues.tock.presentation;

import lombok.extern.slf4j.Slf4j;
import org.aforgues.tock.domain.*;
import org.aforgues.tock.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
        buildModelGameBoard(model, game);
        return "game";
    }

    @GetMapping("/games/{id}/players/{overallRank}/start")
    public String playerStart(@PathVariable String id, @PathVariable int overallRank, Model model) {
        Game game = gameService.playerStart(id, overallRank);
        buildModelGameBoard(model, game);
        return "game";
    }

    @GetMapping("/games/{gameId}/players/{overallRank}/pawns/{pawnNumber}/move/{moveCount}")
    public String movePlayerPawnTo(@PathVariable String gameId, @PathVariable int overallRank,
                                   @PathVariable int pawnNumber, @PathVariable int moveCount, Model model) {
        Game game = gameService.movePlayerPawnTo(gameId, overallRank, pawnNumber, moveCount);
        buildModelGameBoard(model, game);
        return "game";
    }

    @GetMapping("/games/{gameId}/players/{overallRank}/pawns/{pawnNumber}/switch/{targetPawnPosition}")
    public String switchPlayerPawnWith(@PathVariable String gameId, @PathVariable int overallRank,
                                   @PathVariable int pawnNumber, @PathVariable int targetPawnPosition, Model model) {
        Game game = gameService.switchPlayerPawnWith(gameId, overallRank, pawnNumber, targetPawnPosition);
        buildModelGameBoard(model, game);
        return "game";
    }

    @PostMapping("/play")
    public String formPlay(@Valid @ModelAttribute ("playRequest") GamePlayRequest gamePlayRequest,
                           BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            buildModelGameBoard(model, gameService.findByKey(gamePlayRequest.getGameId()));
            return "game";
        }

        String gameId = gamePlayRequest.getGameId();
        String cardId = gamePlayRequest.getCardId();
        Integer pawnNumber = gamePlayRequest.getPawnNumber();
        Integer targetPosition = gamePlayRequest.getTargetPosition();

        try {
            gameService.playCurrentPlayer(gameId, cardId, pawnNumber, targetPosition);
        }
        catch (IllegalPawnMoveException e) {
            errors.reject("illegal_pawn_move","Déplacement impossible (" + e.getMessage() + ")");
            log.warn("Illegal Pawn Move : " + e.getMessage());
            buildModelGameBoard(model, gameService.findByKey(gameId));
            return "game";
        }
        return "redirect:/games/" + gameId;
    }

    @PostMapping("/pass")
    public String formPass(@Valid @ModelAttribute ("passRequest") GamePassRequest gamePassRequest,
                           BindingResult errors, Model model) {
        String gameId = gamePassRequest.getGameId();
        if (errors.hasErrors()) {
            buildModelGameBoard(model, gameService.findByKey(gameId));
            return "game";
        }

        try {
            gameService.passCurrentPlayer(gameId);
        }
        catch (IllegalCardMoveException e) {
            errors.reject("illegal_card_move","Impossible de passer (" + e.getMessage() + ")");
            log.warn("Illegal Card Move : " + e.getMessage());
            buildModelGameBoard(model, gameService.findByKey(gameId));
            return "game";
        }
        return "redirect:/games/" + gameId;
    }

    private void buildModelGameBoard(Model model, Game game) {
        if (game != null) {
            model.addAttribute("gameId", game.getGameId());
            model.addAttribute("currentPlayerPawnColor", game.getCurrentPlayer().getPawnsColor());
            model.addAttribute("gameBoardRows", buildViewModel(game));
            model.addAttribute("currentPlayerCardHand", game.getCurrentPlayerCardHand());
            model.addAttribute("discardPile", game.getDiscardPile());

            // Init forms
            if (model.getAttribute("playRequest") == null) {
                model.addAttribute("playRequest", new GamePlayRequest(game.getGameId()));
            }
            if (model.getAttribute("passRequest") == null) {
                model.addAttribute("passRequest", new GamePassRequest(game.getGameId()));
            }
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
                    fourPlayerGameBoardCellViewModel.setHolePositionCode(HoleType.REGULAR.name() + "-" + hole.getPosition());
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
                    Player player = game.getPlayerByOverallRank(homeHoleData.getPlayerRank());
                    if (homeHoleData.getHoleType() == HoleType.HOME_START) {
                        homeHole = gameBoard.getStartHomeHoleByPlayerAndPosition(player, homeHoleData.getHolePosition());
                    }
                    else {
                        homeHole = gameBoard.getFinishHomeHoleByPlayerAndPosition(player, homeHoleData.getHolePosition());
                    }

                    fourPlayerGameBoardCellViewModel.setHolePositionCode(homeHole.getType().name() + "-"
                                                                        + player.getPawnsColor() + "-"
                                                                        + homeHole.getPosition());
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