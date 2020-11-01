package org.aforgues.tock.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum FourPlayerDistributionStage {
    FIRST(5),
    SECOND(4),
    LAST(4);

    @Getter
    private final int nbCardToDistribute;

    public FourPlayerDistributionStage next() {
        switch (this) {
            case FIRST:
                return SECOND;
            case SECOND:
                return LAST;
            default:
                return FIRST;
        }
    }
}
