package org.aforgues.tock.domain;

import lombok.extern.slf4j.Slf4j;
import org.aforgues.tock.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class GameIntegrationTest {
    Game game4P;

    @BeforeEach
    void init() {
        game4P = new Game(GameType.FOUR_PLAYERS);
    }

    @Test
    void shouldThrowExceptionWhenTryingToEatStakePawnWithFirstPlayer() {
        // Given
        Player currentPlayer = game4P.firstPlayer();
        eatStakeCase(currentPlayer);
    }

    @Test
    void shouldThrowExceptionWhenTryingToEatStakePawnWithLastPlayer() {
        // Given
        Player currentPlayer = game4P.lastPlayer();
        eatStakeCase(currentPlayer);
    }

    private void eatStakeCase(Player currentPlayer) {
        log.debug("Current player : " + currentPlayer.toString());
        currentPlayer.start();
        log.info("Started to play");
        Player nextPlayer = game4P.getNextPlayer(currentPlayer);
        log.info("Next player : " + nextPlayer.toString());
        nextPlayer.start();
        log.info("Started to play");
        currentPlayer.movePawnTo(1, 12);
        log.info("First player moving pawn out of 12 moveCount");

        // When
        log.info("First player moving new pawn out of 6 moveCount => should raise Stake exception");
        Exception exception = Assertions.assertThrows(IllegalPawnMoveException.class, () -> {
            currentPlayer.movePawnTo(1, 6);
        });

        // Then
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Illegal movePawnTo call with targetHole with stake Pawn", exception.getMessage());
    }

    @Test
    void shouldReturnTargetHoleToHomeWhenTryingToEatRegularPawn() {
        // Given
        Player currentPlayer = game4P.firstPlayer();
        currentPlayer.start();

        Player nextPlayer = game4P.getNextPlayer(currentPlayer);
        nextPlayer.start();
        nextPlayer.movePawnTo(1, 2);

        currentPlayer.movePawnTo(1, 13);

        // When
        currentPlayer.movePawnTo(1, 7);

        // Then
        int currentPlayerFirstPawnPosition = currentPlayer.getFirst().getCurrentHoleOnGameBoard().getPosition();
        int expectedPosition = 20;
        Assertions.assertEquals(expectedPosition, currentPlayerFirstPawnPosition);
        HoleType nextPlayerFirstPawnHoleType = nextPlayer.getFirst().getCurrentHoleOnGameBoard().getType();
        HoleType expectedHoleType = HoleType.HOME_START;
        Assertions.assertEquals(expectedHoleType, nextPlayerFirstPawnHoleType);
    }

    @Test
    void shouldThrowExceptionWhenTryingToMovePawnWithFilledPawnInPathFirstPlayer() {
        // Given
        Player currentPlayer = game4P.firstPlayer();
        shouldThrowExceptionWhenTryingToMovePawnWithFilledPawnInPath(currentPlayer, 20);
    }

    @Test
    void shouldThrowExceptionWhenTryingToMovePawnWithFilledPawnInPathLastPlayer() {
        // Given
        Player currentPlayer = game4P.lastPlayer();
        shouldThrowExceptionWhenTryingToMovePawnWithFilledPawnInPath(currentPlayer, 2);
    }

    private void shouldThrowExceptionWhenTryingToMovePawnWithFilledPawnInPath(Player currentPlayer, int conflictPosition) {
        log.debug("Current player : " + currentPlayer.toString());
        currentPlayer.start();
        log.info("Started to play");

        Player nextPlayer = game4P.getNextPlayer(currentPlayer);
        log.info("Next player : " + nextPlayer.toString());
        nextPlayer.start();
        log.info("Started to play");
        nextPlayer.movePawnTo(1, 2);
        log.info("Next player moving pawn out of 2 moveCount");

        currentPlayer.movePawnTo(1, 12);
        log.info("First player moving pawn out of 12 moveCount");

        // When
        log.info("First player moving new pawn out of 9 moveCount => should raise IllegalPawnMoveException");
        Exception exception = Assertions.assertThrows(IllegalPawnMoveException.class, () -> {
            currentPlayer.movePawnTo(1, 9);
        });

        // Then
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Hole with following position is already filled : " + conflictPosition, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPlayerStartWithNoMorePawnsInHomeStart() {
        // Given
        Player currentPlayer = game4P.firstPlayer();
        int pawnNumber = currentPlayer.start();
        currentPlayer.movePawnTo(pawnNumber, 13);

        pawnNumber = currentPlayer.start();
        currentPlayer.movePawnTo(pawnNumber, 12);

        pawnNumber = currentPlayer.start();
        currentPlayer.movePawnTo(pawnNumber, 10);

        pawnNumber = currentPlayer.start();
        currentPlayer.movePawnTo(pawnNumber, 8);

        // When
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> currentPlayer.start());

        // Then
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("No more pawns in home to start for player " + currentPlayer.getPawnsColor(), exception.getMessage());
    }

    @Test
    void shouldEnterInHomeFinishWhenFinishHolesAreFree() {
        // Given
        Player currentPlayer = game4P.firstPlayer();
        int pawnNumber = currentPlayer.start();
        currentPlayer.movePawnTo(pawnNumber, -4);

        // When
        Hole targetHole = currentPlayer.movePawnTo(pawnNumber, 6);

        // Then
        Assertions.assertEquals(HoleType.HOME_FINISH, targetHole.getType());
        Assertions.assertEquals(4, targetHole.getPosition());
    }


    @Test
    void shouldStopAtHomeEntrance() {
        // Given
        Player currentPlayer = game4P.firstPlayer();
        int pawnNumber = currentPlayer.start();
        currentPlayer.movePawnTo(pawnNumber, -4);

        // When
        Hole targetHole = currentPlayer.movePawnTo(pawnNumber, 2);

        // Then
        Assertions.assertEquals(HoleType.HOME_ENTRANCE, targetHole.getType());
        Assertions.assertEquals(70, targetHole.getPosition());
    }

    @Test
    void shouldNotEnterInHomeFinishWhenMoveCountIsTooHigh() {
        // Given
        Player currentPlayer = game4P.firstPlayer();
        int pawnNumber = currentPlayer.start();
        currentPlayer.movePawnTo(pawnNumber, -4);

        // When
        Hole targetHole = currentPlayer.movePawnTo(pawnNumber, 7);

        // Then
        Assertions.assertEquals(HoleType.REGULAR, targetHole.getType());
        Assertions.assertEquals(3, targetHole.getPosition());
    }

    @Test
    void shouldNotEnterInHomeFinishWhenHomeEntranceBelongsToAnotherPlayer() {
        // Given
        Player currentPlayer = game4P.firstPlayer();
        int pawnNumber = currentPlayer.start();
        currentPlayer.movePawnTo(pawnNumber, 13);

        // When
        Hole targetHole = currentPlayer.movePawnTo(pawnNumber, 7);

        // Then
        Assertions.assertEquals(HoleType.REGULAR, targetHole.getType());
        Assertions.assertEquals(20, targetHole.getPosition());
    }

    @Test
    void shouldNotEnterInHomeFinishWhenFinishHolesAreNotFree() {
        // Given
        Player currentPlayer = game4P.firstPlayer();
        int pawnNumber = currentPlayer.start();
        currentPlayer.movePawnTo(pawnNumber, -4);
        currentPlayer.movePawnTo(pawnNumber, 5);

        pawnNumber = currentPlayer.start();
        currentPlayer.movePawnTo(pawnNumber, -4);

        // When
        Hole targetHole = currentPlayer.movePawnTo(pawnNumber, 6);

        // Then
        Assertions.assertEquals(HoleType.REGULAR, targetHole.getType());
        Assertions.assertEquals(2, targetHole.getPosition());
    }

    @Test
    void shouldNotEnterInHomeFinishWhenTargetHoleIsNotFree() {
        // Given
        Player currentPlayer = game4P.firstPlayer();
        int pawnNumber = currentPlayer.start();
        currentPlayer.movePawnTo(pawnNumber, -4);
        currentPlayer.movePawnTo(pawnNumber, 6);

        pawnNumber = currentPlayer.start();
        currentPlayer.movePawnTo(pawnNumber, -4);

        // When
        Hole targetHole = currentPlayer.movePawnTo(pawnNumber, 6);

        // Then
        Assertions.assertEquals(HoleType.REGULAR, targetHole.getType());
        Assertions.assertEquals(2, targetHole.getPosition());
    }

    @Test
    void shouldThrowExceptionWhenSwitchingPawnWithEmptyTargetHole() {
        // Given
        Player currentPlayer = game4P.firstPlayer();
        int pawnNumber = currentPlayer.start();
        currentPlayer.movePawnTo(pawnNumber, 6);

        Player otherPlayer = game4P.lastPlayer();
        int otherPawnNumber = otherPlayer.start();
        otherPlayer.movePawnTo(otherPawnNumber, 3);

        // When
        final int targetSwitchPawnPosition = 10;
        Exception exception = Assertions.assertThrows(IllegalPawnMoveException.class, () -> currentPlayer.switchPawns(pawnNumber, targetSwitchPawnPosition));

        // Then
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Pawn::switchWithPawnAt : no pawn found at target hole position : " + targetSwitchPawnPosition, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSwitchingPawnWithSamePlayerTargetHole() {
        // Given
        Player currentPlayer = game4P.firstPlayer();
        int pawnNumber = currentPlayer.start();
        final int targetSwitchPawnPosition = 6;
        currentPlayer.movePawnTo(pawnNumber, targetSwitchPawnPosition);

        final int nextPawnNumber = currentPlayer.start();

        // When
        Exception exception = Assertions.assertThrows(IllegalPawnMoveException.class, () -> currentPlayer.switchPawns(nextPawnNumber, targetSwitchPawnPosition));

        // Then
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Pawn::switchWithPawnAt : illegal switch with a pawn belonging to the same player", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSwitchingPawnWithStakeTargetHole() {
        // Given
        Player currentPlayer = game4P.firstPlayer();
        int pawnNumber = currentPlayer.start();
        currentPlayer.movePawnTo(pawnNumber, 6);

        Player otherPlayer = game4P.lastPlayer();
        otherPlayer.start();

        // When
        final int targetSwitchPawnPosition = 54; // Stake position of last player
        Exception exception = Assertions.assertThrows(IllegalPawnMoveException.class, () -> currentPlayer.switchPawns(pawnNumber, targetSwitchPawnPosition));

        // Then
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Pawn::switchWithPawnAt : illegal switch with a stake pawn", exception.getMessage());
    }

    @Test
    void shouldSwitchPawnsWhenConditionsAreMet() {
        // Given
        Player currentPlayer = game4P.firstPlayer();
        int pawnNumber = currentPlayer.start();
        Hole firstHole = currentPlayer.movePawnTo(pawnNumber, 6);

        Player otherPlayer = game4P.lastPlayer();
        int otherPawnNumber = otherPlayer.start();
        final int otherPawnMove = 3;
        Hole otherHole = otherPlayer.movePawnTo(otherPawnNumber, otherPawnMove);

        // When
        final int targetSwitchPawnPosition = 54 + otherPawnMove;
        currentPlayer.switchPawns(pawnNumber, targetSwitchPawnPosition);

        // Then
        Assertions.assertEquals(otherPlayer.getFirst(), firstHole.getOccupant());
        Assertions.assertEquals(currentPlayer.getFirst(), otherHole.getOccupant());
    }

    @Test
    void shouldMovePawn4PositionBehind() {
        // Given
        Player currentPlayer = game4P.firstPlayer();
        int pawnNumber = currentPlayer.start();

        Player thirdPlayer = game4P.getPlayerByOverallRank(3);
        thirdPlayer.start();

        // When
        Hole hole = currentPlayer.movePawnTo(pawnNumber, -4);

        // Then
        Assertions.assertEquals(68, hole.getPosition());
    }
}
