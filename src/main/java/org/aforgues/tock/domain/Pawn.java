package org.aforgues.tock.domain;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Pawn {
    @Getter
    private Player owner;

    @Getter
    private Hole currentHoleOnGameBoard;

    @Getter
    private boolean hasJustGoneOutOfHome;

    public Pawn(Player owner) {
        this.owner = owner;
    }

    public boolean isAtHome() {
        return HoleType.HOME_START.equals(this.currentHoleOnGameBoard.getType());
    }

    public boolean isInTheGame() {
        return HoleType.STAKE_ELIGIBLE.equals(this.currentHoleOnGameBoard.getType())
                || HoleType.REGULAR.equals(this.currentHoleOnGameBoard.getType())
                || HoleType.HOME_ENTRANCE.equals(this.currentHoleOnGameBoard.getType());
    }

    public boolean isInHomeFinish() {
        return HoleType.HOME_FINISH.equals(this.currentHoleOnGameBoard.getType());
    }

    // FIXME : moveTo is ambiguous comparing with other public method movePawn
    // should be called only by Hole::setSpecificOccupant
    public void moveTo(Hole targetHole) {
        if (this.currentHoleOnGameBoard != null){
            log.trace("Current hole position " + this.currentHoleOnGameBoard.getPosition());

            if (this.currentHoleOnGameBoard.getType().equals(HoleType.HOME_START)
            && targetHole.getType().equals(HoleType.STAKE_ELIGIBLE)
            && targetHole.getAssociatedPlayer().equals(this.owner))
                this.hasJustGoneOutOfHome = true;
            else
                this.hasJustGoneOutOfHome = false;
        }

        this.currentHoleOnGameBoard = targetHole;

        if (this.currentHoleOnGameBoard != null) {
            log.trace("New hole position " + this.currentHoleOnGameBoard.getPosition());
        }
    }

    public int getPlayerPawnNumber() {
        if (this.owner.getFirst().equals(this))
            return 1;
        else if (this.owner.getSecond().equals(this))
            return 2;
        else if (this.owner.getThird().equals(this))
            return 3;
        return 4;
    }

    // TODO : complete with future Cards hand condition
    public boolean isPlayable() {
        return this.getOwner().isCurrentPlayer();
    }

    public void start() {
        Hole targetHole = this.currentHoleOnGameBoard.getGameBoard().getStakeEligibleHole(this.getOwner());
        this.movePawnTo(targetHole);
    }

    private Hole getTargetHoleAfterMove(int moveCount) {
        // check allowed move count
        checkMoveCount(moveCount);

        // Compute target hole
        if (isInTheGame()) {
            int targetPosition = this.currentHoleOnGameBoard.getPosition() + moveCount;

            int maxHoleOnGameBoard = this.currentHoleOnGameBoard.getGameBoard().getMaxHolesOnGameBoard();
            if (targetPosition < 0)
                targetPosition += maxHoleOnGameBoard;

            // Check if there is the Home Entrance hole in the path
            for (int upcomingPosition = this.currentHoleOnGameBoard.getPosition() + 1; upcomingPosition < targetPosition; upcomingPosition++) {
                Hole upcomingHole = this.currentHoleOnGameBoard.getGameBoard().getHoleByPosition(upcomingPosition);
                if (HoleType.HOME_ENTRANCE == upcomingHole.getType() && upcomingHole.getAssociatedPlayer().equals(this.getOwner())) {
                    // Computing remaining move count
                    int remainingMoveCount = targetPosition - upcomingPosition;
                    if (remainingMoveCount > 4) {
                        log.info("Moving in front of Home Entrance but moveCount is too high to enter in Home Finish !");
                        break;
                    }
                    else {
                        Hole targetHole = this.currentHoleOnGameBoard.getGameBoard().getFinishHomeHoleByPlayerAndPosition(this.owner, remainingMoveCount);
                        try {
                            checkThatMoveIsAllowed(targetHole);
                        }
                        catch (IllegalPawnMoveException e) {
                            break;
                        }
                        return targetHole;
                    }
                }

            }

            Hole targetHole = this.currentHoleOnGameBoard.getGameBoard().getHoleByPosition(targetPosition);
            return targetHole;
        }
        else {
            throw new IllegalPawnMoveException("Pawn::getTargetHoleAfterMove should be called only with pawn in classic hole => actual : " + this.currentHoleOnGameBoard.getType());
        }
    }

    void checkMoveCount(int moveCount) {
        // TODO : create enum with all cards with associated move count
        if (moveCount != -4 && !(moveCount >= 1 && moveCount < 4) && !(moveCount >4 && moveCount <= 13))
            throw new IllegalPawnMoveException("Pawn::getTargetHoleAfterMove called with illegal parameter moveCount : " + moveCount);
    }

    public void movePawn(int moveCount) {
        Hole targetHole = this.getTargetHoleAfterMove(moveCount);
        movePawnTo(targetHole);
    }

    private void movePawnTo(Hole targetHole) {
        // Check that the move is allowed : no other pawns in the path
        checkThatMoveIsAllowed(targetHole);

        if (! targetHole.isFree()) {
            log.info("TargetHole is not free (position : " + targetHole.getPosition() + ")");
            if (targetHole.hasStakeOccupant())
                throw new IllegalPawnMoveException("Illegal movePawnTo call with targetHole with stake Pawn");
            else {
                log.info("Eating pawn in target hole : " + targetHole.getOccupant());
                targetHole.getOccupant().returnPawnToHome();
            }
        }

        // Remove pawn from original hole
        this.currentHoleOnGameBoard.setSpecificOccupant(null);

        // Moving to target hole
        targetHole.setSpecificOccupant(this);
    }

    private void checkThatMoveIsAllowed(Hole targetHole) {
        // In case of pawn returning to home
        if (HoleType.HOME_START.equals(targetHole.getType()))
            return;

        // TODO : in case of pawn switch => Jack of hearts (coeur) / spades (pique) / clubs (trèfle) / diamonds (carreau)

        HoleType holeType = this.currentHoleOnGameBoard.getType();
        switch (holeType) {
            case HOME_START:
                checkHomeStartMove(targetHole);
                break;
            case HOME_FINISH:
                checkHomeFinishMove(targetHole);
                break;
            default:
                checkStandardMove(targetHole);
        }
    }

    private void checkStandardMove(Hole targetHole) {
        int currentPawnPosition = this.currentHoleOnGameBoard.getPosition();
        int targetPosition = targetHole.getPosition();
        GameBoard gameBoard = this.currentHoleOnGameBoard.getGameBoard();

        if (targetHole.getType() == HoleType.HOME_FINISH) {
            int entrancePosition = gameBoard.getHomeEntranceHolePositionByPlayer(this.owner);

            // check that holes in finish lines are free including targetPosition
            for (int position = 1; position <= targetPosition; position++) {
                if (! gameBoard.getFinishHomeHoleByPlayerAndPosition(this.owner, position).isFree())
                    throw new IllegalPawnMoveException("FinishHome hole with following position is already filled : " + position);
            }

            // check that previous holes are also free
            targetPosition = entrancePosition;
        }

        // Special case of -4
        if ((targetPosition - currentPawnPosition + 4) % gameBoard.getMaxHolesOnGameBoard() == 0) {
            int tempPosition = targetPosition;
            targetPosition = currentPawnPosition;
            currentPawnPosition = tempPosition;
        }

        if (targetPosition > currentPawnPosition) {
            for (int position = currentPawnPosition + 1; position < targetPosition; position++) {
                if (! gameBoard.getHoleByPosition(position).isFree())
                    throw new IllegalPawnMoveException("Hole with following position is already filled : " + position);
            }
        }
        else {
            for (int position = currentPawnPosition + 1; position < gameBoard.getMaxHolesOnGameBoard(); position++) {
                if (! gameBoard.getHoleByPosition(position).isFree())
                    throw new IllegalPawnMoveException("Hole with following position is already filled : " + position);
            }

            for (int position = 0; position < targetPosition; position++) {
                if (! gameBoard.getHoleByPosition(position).isFree())
                    throw new IllegalPawnMoveException("Hole with following position is already filled : " + position);
            }
        }
    }

    private void checkHomeStartMove(Hole targetHole) {
        if (! HoleType.HOME_START.equals(this.currentHoleOnGameBoard.getType()))
            throw new IllegalPawnMoveException("HomeStart move : Only pawn from HomeStart hole can go out of home");

        if (! HoleType.STAKE_ELIGIBLE.equals(targetHole.getType()))
            throw new IllegalPawnMoveException("HomeStart move : Target hole must be a StakeEligible hole type => actual : " + targetHole.getType());

        if (! this.owner.equals(targetHole.getAssociatedPlayer()))
            throw new IllegalPawnMoveException("HomeStart move : Target hole must be associated to pawn player => actual : " + targetHole.getAssociatedPlayer());
    }

    private void checkHomeFinishMove(Hole targetHole) {
        if (! HoleType.HOME_FINISH.equals(targetHole.getType()))
            throw new IllegalPawnMoveException("HomeFinish move : Target hole me be a HomeFinish hole type => actual : " + targetHole.getType());
    }

    private void returnPawnToHome() {
        log.info("Return pawn to its Home : " + this);
        Player player = this.owner;
        movePawnTo(this.currentHoleOnGameBoard.getGameBoard().getFirstHomeStartFreeHole(player));
    }

    public void switchWithPawnAt(int targetPawnPosition) {
        Hole targetHole = this.currentHoleOnGameBoard.getGameBoard().getHoleByPosition(targetPawnPosition);
        if (targetHole.isFree())
            throw new IllegalPawnMoveException("Pawn::switchWithPawnAt : no pawn found at target hole position : " + targetPawnPosition);

        Pawn targetPawn = targetHole.getOccupant();
        if (targetPawn.getOwner().equals(this.getOwner()))
            throw new IllegalPawnMoveException("Pawn::switchWithPawnAt : illegal switch with a pawn belonging to the same player");

        if (targetHole.hasStakeOccupant())
            throw new IllegalPawnMoveException("Pawn::switchWithPawnAt : illegal switch with a stake pawn");

        this.getCurrentHoleOnGameBoard().setSpecificOccupant(targetPawn);
        targetHole.setSpecificOccupant(this);
    }

    @Override
    public String toString() {
        return this.owner.getPawnsColor() + " pawn (" + getPlayerPawnNumber() + ")";
    }
}
