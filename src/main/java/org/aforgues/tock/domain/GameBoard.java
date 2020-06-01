package org.aforgues.tock.domain;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class GameBoard {
    private static final int MAX_HOLES_COUNT = 18;

    private Game ongoingGame;
    private Map<Player, List<Hole>> homeHolesByPlayer;
    @Getter
    private Set<Hole> holes;
    private Map<Player, List<Hole>> finishHolesByPlayer;


    public GameBoard(Set<Team> teams, Game game) {
        this.ongoingGame = game;
        this.homeHolesByPlayer = new TreeMap<>();
        this.holes = new TreeSet<>();
        this.finishHolesByPlayer = new TreeMap<>();
        initGameBoard(teams);
    }

    private void initGameBoard(Set<Team> teams) {
        for (Team team : teams) {
            for (Player player : team.getPlayers()) {
                addHolesToHome(player);

                // Compute position offset
                int positionOffset = (player.getOverallRank() - 1) * MAX_HOLES_COUNT;

                addHoleToGameBoard(HoleType.STAKE_ELIGIBLE, positionOffset, player);
                for (int position = 1; position < MAX_HOLES_COUNT; position++) {
                    if (position == MAX_HOLES_COUNT - 2) {
                        Player associatedPlayer = getAssociatedPlayer(player);
                        addHoleToGameBoard(HoleType.HOME_ENTRANCE, positionOffset + position, associatedPlayer);
                    } else {
                        addHoleToGameBoard(HoleType.REGULAR, positionOffset + position, null);
                    }
                }

                addHolesToFinish(player);
            }
        }
    }

    private Player getAssociatedPlayer(Player currentPlayer) {
        return this.ongoingGame.getNextPlayer(currentPlayer);
    }

    private void addHolesToHome(Player player) {
        List<Hole> homeHoles = this.homeHolesByPlayer.get(player);
        if (homeHoles == null) {
            homeHoles = new ArrayList<>();
            this.homeHolesByPlayer.put(player, homeHoles);
        }

        homeHoles.add(computeHole(HoleType.HOME_START, 1, player.getFirst(), player));
        homeHoles.add(computeHole(HoleType.HOME_START, 2, player.getSecond(), player));
        homeHoles.add(computeHole(HoleType.HOME_START, 3, player.getThird(), player));
        homeHoles.add(computeHole(HoleType.HOME_START, 4, player.getFourth(), player));
    }

    private void addHolesToFinish(Player player) {
        List<Hole> finishHoles = this.finishHolesByPlayer.get(player);
        if (finishHoles == null) {
            finishHoles = new ArrayList<>();
            this.finishHolesByPlayer.put(player, finishHoles);
        }
        finishHoles.add(computeHole(HoleType.HOME_FINISH, 1, null, player));
        finishHoles.add(computeHole(HoleType.HOME_FINISH, 2, null, player));
        finishHoles.add(computeHole(HoleType.HOME_FINISH, 3, null, player));
        finishHoles.add(computeHole(HoleType.HOME_FINISH, 4, null, player));
    }

    private void addHoleToGameBoard(HoleType holeType, Integer position, Player associatedPlayer) {
        this.holes.add(computeHole(holeType, position, null, associatedPlayer));
    }

    private Hole computeHole(HoleType type, Integer position, Pawn pawn, Player player) {
        Hole hole = new Hole(type, position, this);
        if (pawn != null)
            hole.setSpecificOccupant(pawn);
        if (player != null)
            hole.setAssociatedPlayer(player);
        return hole;
    }

    public Hole getFirstHomeStartFreeHole(Player player) {
        return this.homeHolesByPlayer.get(player)
                .stream()
                .filter(hole -> hole.isFree())
                .findFirst()
                .get();
    }

    public Hole getStakeEligibleHole(Player player) {
        return this.holes.stream()
                .filter(hole -> hole.getType().equals(HoleType.STAKE_ELIGIBLE))
                .filter(hole -> player.equals(hole.getAssociatedPlayer()))
                .findFirst()
                .get();
    }

    public Hole getHoleByPosition(int position) {
        if (position < 0)
            throw new IllegalArgumentException("GameBoard::getHoleByPosition : Illegal position parameter " + position + " => should be between 0 and " + (this.holes.size() - 1));

        if (position >= this.holes.size()) {
            log.debug("Hole position overflow : " + position);
            position = position % this.holes.size();
            log.debug("=> computing relative hole position : " + position);
        }

        int relativePosition = position;
        return this.holes.stream()
                .filter(hole -> hole.getPosition() == relativePosition)
                .findFirst()
                .get();
    }

    public Hole getStartHomeHoleByPlayerAndPosition(Player player, int position) {
        if (position < 0 || position > 4)
            throw new IllegalArgumentException("GameBoard::getStartHomeHoleByPlayerAndPosition : Illegal position parameter => should be between 1 and 4, actual : " + position);

        List<Hole> startHoles = this.homeHolesByPlayer.get(player);
        if (startHoles != null && startHoles.isEmpty())
            throw new IllegalArgumentException("GameBoard::getStartHomeHoleByPlayerAndPosition : Illegal player parameter => no start holes found for player : " + player);

        return startHoles.stream()
                .filter(hole -> hole.getPosition() == position)
                .findFirst()
                .get();
    }

    public Hole getFinishHomeHoleByPlayerAndPosition(Player player, int position) {
        if (position < 0 || position > 4)
            throw new IllegalArgumentException("GameBoard::getFinishHomeHoleByPlayerAndPosition : Illegal position parameter => should be between 1 and 4, actual : " + position);

        List<Hole> finishHoles = this.finishHolesByPlayer.get(player);
        if (finishHoles != null && finishHoles.isEmpty())
            throw new IllegalArgumentException("GameBoard::getFinishHomeHoleByPlayerAndPosition : Illegal player parameter => no finish holes found for player : " + player);

        return finishHoles.stream()
                .filter(hole -> hole.getPosition() == position)
                .findFirst()
                .get();
    }

    public int getHomeEntranceHolePositionByPlayer(Player player) {
        return this.holes.stream()
                .filter(hole -> hole.getType() == HoleType.HOME_ENTRANCE)
                .filter(hole -> hole.getAssociatedPlayer().equals(player))
                .map(hole -> hole.getPosition())
                .findFirst()
                .get();
    }

    public int getMaxHolesOnGameBoard() {
        return this.holes.size();
    }

    @Override
    public String toString() {
        StringBuilder display = new StringBuilder();
        display.append("*********** HOME HOLES ***************");
        display.append("\n");
        for (Map.Entry<Player, List<Hole>> entry : this.homeHolesByPlayer.entrySet()) {
            Player player = entry.getKey();
            display.append(player);
            display.append("\n");
            for (Hole hole : entry.getValue()) {
                display.append(hole);
                display.append("\n");
            }
        }

        display.append("\n");
        display.append("*********** REGULAR HOLES ***************");
        display.append("\n");
        for (Hole hole : this.holes) {
            display.append(hole);
            display.append("\n");
        }

        display.append("\n");
        display.append("*********** FINISH HOLES ***************");
        display.append("\n");
        for (Map.Entry<Player, List<Hole>> entry : this.finishHolesByPlayer.entrySet()) {
            Player player = entry.getKey();
            display.append(player);
            display.append("\n");
            for (Hole hole : entry.getValue()) {
                display.append(hole);
                display.append("\n");
            }
        }

        System.out.print(display.toString());
        return display.toString();
    }
}
