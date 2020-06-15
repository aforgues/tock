package org.aforgues.tock.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class Card {
    private static final String ID_SEPARATOR = "-";

    private CardValue cardValue;
    private CardColor cardColor;

    public String getCardId() {
        return cardColor.name().toLowerCase() + ID_SEPARATOR + cardValue.name().toLowerCase();
    }

    public static Card from(String cardId) {
        if (cardId == null || ! cardId.contains(ID_SEPARATOR))
            throw new IllegalArgumentException("Card::from called with invalid cardId : " + cardId);

        String[] cardDatas = cardId.toUpperCase().split(ID_SEPARATOR);
        return new Card(CardValue.valueOf(cardDatas[1]), CardColor.valueOf(cardDatas[0]));
    }

    @Getter
    public enum CardValue {
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

        CardValue(Integer moveCount, boolean canStart, boolean canSplit, boolean canSwitch) {
            this.moveCount = moveCount;
            this.canStart = canStart;
            this.canSplit = canSplit;
            this.canSwitch = canSwitch;
        }

        CardValue(int moveCount) {
            this(moveCount, false, false, false);
        }
    }

    @Getter
    public enum CardColor {
        SPADES,
        HEARTS,
        DIAMONDS,
        CLUBS
    }
}