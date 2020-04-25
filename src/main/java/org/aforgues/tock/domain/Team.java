package org.aforgues.tock.domain;

import lombok.Getter;

import java.util.List;

public class Team  implements Comparable<Team> {
    @Getter
    private String name;
    @Getter
    private Player fistPlayer;
    @Getter
    private Player secondPlayer;

    @Getter
    private final int rank;

    @Getter
    private Game ongoingGame;

    public Team(Game game, String name, final int rank) {
        this.name = name;
        this.rank = rank;
        this.ongoingGame = game;
        this.initPlayers();
    }

    public List<Player> getPlayers() {
        return List.of(fistPlayer, secondPlayer);
    }

    private void initPlayers() {
        this.fistPlayer = new Player(this,"Joueur 1", 1);
        this.secondPlayer = new Player(this,"Joueur 2", 2);
    }

    @Override
    public int compareTo(Team o) {
        if (this.rank > o.getRank())
            return 1;
        else if (this.rank < o.getRank())
            return -1;
        return 0;
    }
}
