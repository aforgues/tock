package org.aforgues.tock.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@Slf4j
public class Player implements Comparable<Player> {
    @Setter
    private String name;

    @Getter
    private final int rank;

    private Pawn first;
    private Pawn second;
    private Pawn third;
    private Pawn fourth;

    private final Team team;

    public Player(final Team team, String name, final int rank) {
        this.team = team;
        this.name = name;
        this.rank = rank;
        first = new Pawn(this);
        second = new Pawn(this);
        third = new Pawn(this);
        fourth = new Pawn(this);
    }

    /**
     * Global playing order
     * Team A (rank 1), player 1 (rank 1)
     * Team B (rank 2), player 1 (rank 1)
     * [Team C (rank 3), player 1 (rank 1)]
     * Team A (rank 1), player 2 (rank 2)
     * Team B (rank 2), player 2 (rank 2)
     * [Team C (rank 3], player 2 (rank) 2)
     */

    public int getOverallRank() {
        int teamRank = this.team.getRank();
        switch(this.team.getOngoingGame().getType()) {
            case FOUR_PLAYERS:
                return teamRank + (this.rank == 1 ? 0 : this.rank);
            case SIX_PLAYERS:
                return teamRank + (this.rank == 1 ? 0 : this.rank + 1);
            default:
                throw new IllegalArgumentException("Unknown game type while computing player overallRank " + this.team.getOngoingGame().getType());
        }

    }

    public String getPawnsColor() {
        switch (this.getOverallRank()) {
            case 1: return "Blue";
            case 2: return "Green";
            case 3: return "Red";
            case 4: return "Yellow";
            case 5: return "Purple";
            case 6: return "White";
            default : throw new IndexOutOfBoundsException("Player overall rank is beyond maximum number of players allowed : " + this.getOverallRank());
        }
    }

    /**
     * Service methods => to refactor in PlayerService
     */

    public int start() {
        List<Pawn> pawns = getPawnsStillInHome();
        if (pawns != null && ! pawns.isEmpty()) {
            Pawn pawn = pawns.get(0);
            pawn.start();
            return pawn.getPlayerPawnNumber();
        }
        else {
            throw new IllegalArgumentException("No more pawns in home to start for player " + this.getPawnsColor());
        }
    }

    private List<Pawn> getPawnsStillInHome() {
        return List.of(getFirst(), getSecond(), getThird(), getFourth())
                .stream()
                .filter(pawn -> pawn.getCurrentHoleOnGameBoard().getType().equals(HoleType.HOME_START))
                .collect(Collectors.toList());
    }

    private Pawn getPawn(int playerPawnNumber) {
        Pawn pawn;
        switch (playerPawnNumber) {
            case 1:
                pawn = getFirst();
                break;
            case 2:
                pawn = getSecond();
                break;
            case 3:
                pawn = getThird();
                break;
            case 4:
                pawn = getFourth();
                break;
            default:
                throw new IllegalArgumentException("Illegal pawn number : " + playerPawnNumber);
        }
        return pawn;
    }

    public Hole movePawnTo(int pawnNumber, int moveCount) {
        Pawn movingPawn = getPawn(pawnNumber);
        movingPawn.movePawn(moveCount);
        return movingPawn.getCurrentHoleOnGameBoard();
    }

    public void switchPawns(int pawnNumber, int targetPawnPosition) {
        Pawn movingPawn = getPawn(pawnNumber);
        movingPawn.switchWithPawnAt(targetPawnPosition);
    }

    @Override
    public int compareTo(Player other) {
        if (this.getOverallRank() > other.getOverallRank())
            return 1;
        else if (this.getOverallRank() < other.getOverallRank())
            return -1;
        return 0;
    }

    @Override
    public String toString() {
        return "Team " + this.getTeam().getName() + " - player " + this.getName() + " (" + this.getPawnsColor() + ")";
    }

    public boolean isCurrentPlayer() {
        return this.team.getOngoingGame().getCurrentPlayer().equals(this);
    }
}
