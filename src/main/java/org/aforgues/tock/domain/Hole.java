package org.aforgues.tock.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Hole implements Comparable<Hole> {
    private HoleType type;
    private Integer position;
    private Pawn occupant;
    private Player associatedPlayer;
    @ToString.Exclude
    private GameBoard gameBoard;

    public Hole(HoleType holeType, Integer position, GameBoard gameBoard) {
        this.type = holeType;
        this.position = position;
        this.gameBoard = gameBoard;
    }

    public void setSpecificOccupant(Pawn occupant) {
        this.occupant = occupant;
        if (occupant != null) {
            this.occupant.moveTo(this);
        }
    }

    public void setAssociatedPlayer(Player associatedPlayer) {
        switch (type) {
            case STAKE_ELIGIBLE:
            case HOME_ENTRANCE:
                this.associatedPlayer = associatedPlayer;
                break;
            default:
                throw new IllegalArgumentException("No associated player allowed with this kind of hole type : " + type);
        }
    }

    public boolean isFree() {
        return this.occupant == null;
    }

    public boolean hasStakeOccupant() {
        return ! isFree() && this.occupant.isHasJustGoneOutOfHome();
    }

    @Override
    public int compareTo(Hole other) {
        return this.position.compareTo(other.position);
    }
}
