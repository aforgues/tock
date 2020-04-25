package org.aforgues.tock.domain;

import org.aforgues.tock.domain.IllegalPawnMoveException;
import org.aforgues.tock.domain.Pawn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class PawnTest {

    Pawn pawn;

    @BeforeEach
    void init() {
        pawn = new Pawn(null);
    }

    @Test
    void checkMoveCount_shouldThrowExceptionWithNegativeMoveCount() {
        // Given
        int moveCount = -1;

        // When
        Exception exception = Assertions.assertThrows(IllegalPawnMoveException.class, () -> pawn.checkMoveCount(moveCount));

        // Then
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Pawn::getTargetHoleAfterMove called with illegal parameter moveCount : " + moveCount, exception.getMessage());
    }

    @Test
    void checkMoveCount_shouldThrowExceptionWithTooHighMoveCount() {
        // Given
        int moveCount = 14;

        // When
        Exception exception = Assertions.assertThrows(IllegalPawnMoveException.class, () -> pawn.checkMoveCount(moveCount));

        // Then
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Pawn::getTargetHoleAfterMove called with illegal parameter moveCount : " + moveCount, exception.getMessage());
    }

    @Test
    void checkMoveCount_shouldThrowExceptionWithIllegal4MoveCount() {
        // Given
        int moveCount = 4;

        // When
        Exception exception = Assertions.assertThrows(IllegalPawnMoveException.class, () -> pawn.checkMoveCount(moveCount));

        // Then
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Pawn::getTargetHoleAfterMove called with illegal parameter moveCount : " + moveCount, exception.getMessage());
    }

    @Test
    void checkMoveCount_shouldNotThrowExceptionWithCorrectMoveCount() {
        // Given
        List<Integer> moveCounts = List.of(-4, 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13);

        // When
        // Then
        for (int moveCount : moveCounts)
            Assertions.assertDoesNotThrow(() -> pawn.checkMoveCount(moveCount));
    }
}