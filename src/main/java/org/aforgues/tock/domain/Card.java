package org.aforgues.tock.domain;

import lombok.Getter;

@Getter
public enum Card {
    AS(1, true, false, false),
    TWO(2),
    THREE(3),
    FOUR(-4),
    FIVE(5),
    SIX(6),
    SEVEN(7, false, true, false),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(null, false, false, true),
    QUEEN(12),
    KING(13, true, false, false);

    private Integer moveCount;
    private boolean canStart;
    private boolean canSplit;
    private boolean canSwitch;

    Card(Integer moveCount, boolean canStart, boolean canSplit, boolean canSwitch) {
        this.moveCount = moveCount;
        this.canStart = canStart;
        this.canSplit = canSplit;
        this.canSwitch = canSwitch;
    }

    Card(int moveCount) {
        this(moveCount, false, false, false);
    }
}
