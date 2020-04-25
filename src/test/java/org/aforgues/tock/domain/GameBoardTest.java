package org.aforgues.tock.domain;

import org.aforgues.tock.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameBoardTest {
    GameBoard gameBoard4P;
    GameBoard gameBoard6P;

    @BeforeEach
    public void init() {
        Game game4P = new Game(GameType.FOUR_PLAYERS);
        gameBoard4P = game4P.getGameBoard();
        Game game6P = new Game(GameType.SIX_PLAYERS);
        gameBoard6P = game6P.getGameBoard();
    }

    @Test
    void getHoleByPositionWithIllegalPosition4Players() {
        // Given
        int illegalPosition = -1;

        // When
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> gameBoard4P.getHoleByPosition(illegalPosition));

        // Then
        Assertions.assertNotNull(exception);
        String expectedMessage = "GameBoard::getHoleByPosition : Illegal position parameter " + illegalPosition + " => should be between 0 and " + (gameBoard4P.getMaxHolesOnGameBoard() - 1);
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void getHoleByPositionWithOverflowPositionEdgeCase4Players() {
        // Given
        int overflowPosition = 72;

        // When
        Hole hole = gameBoard4P.getHoleByPosition(overflowPosition);

        // Then
        Assertions.assertNotNull(hole);
        Assertions.assertEquals(0, hole.getPosition());
    }

    @Test
    void getHoleByPositionWithOverflowPositionOtherCase4Players() {
        // Given
        int overflowPosition = 76;

        // When
        Hole hole = gameBoard4P.getHoleByPosition(overflowPosition);

        // Then
        Assertions.assertNotNull(hole);
        Assertions.assertEquals(4, hole.getPosition());
    }

    @Test
    void getHoleByPositionWithIllegalPosition6Players() {
        // Given
        int illegalPosition = -1;

        // When
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            gameBoard6P.getHoleByPosition(illegalPosition);
        });

        // Then
        Assertions.assertNotNull(exception);
        String expectedMessage = "GameBoard::getHoleByPosition : Illegal position parameter " + illegalPosition + " => should be between 0 and " + (gameBoard6P.getMaxHolesOnGameBoard() - 1);
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void getHoleByPositionWithOverflowPositionEdgeCase6Players() {
        // Given
        int overflowPosition = 108;

        // When
        Hole hole = gameBoard6P.getHoleByPosition(overflowPosition);

        // Then
        Assertions.assertNotNull(hole);
        Assertions.assertEquals(0, hole.getPosition());
    }

    @Test
    void getHoleByPositionWithOverflowPositionOtherCase6Players() {
        // Given
        int overflowPosition = 112;

        // When
        Hole hole = gameBoard6P.getHoleByPosition(overflowPosition);

        // Then
        Assertions.assertNotNull(hole);
        Assertions.assertEquals(4, hole.getPosition());
    }

    @Test
    void getHoleByPositionWithRegularPosition() {
        // Given
        int regularPosition = 12;

        // When
        Hole hole = gameBoard4P.getHoleByPosition(regularPosition);

        // Then
        Assertions.assertNotNull(hole);
        Assertions.assertEquals(HoleType.REGULAR, hole.getType());
    }

    @Test
    void getHoleByPositionWithStakeEligiblePosition() {
        // Given
        int regularPosition = 18;

        // When
        Hole hole = gameBoard4P.getHoleByPosition(regularPosition);

        // Then
        Assertions.assertNotNull(hole);
        Assertions.assertEquals(HoleType.STAKE_ELIGIBLE, hole.getType());
    }

    @Test
    void getHoleByPositionWithHomeEntrancePosition() {
        // Given
        int regularPosition = 16;

        // When
        Hole hole = gameBoard4P.getHoleByPosition(regularPosition);

        // Then
        Assertions.assertNotNull(hole);
        Assertions.assertEquals(HoleType.HOME_ENTRANCE, hole.getType());
    }
}