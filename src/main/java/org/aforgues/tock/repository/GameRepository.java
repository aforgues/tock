package org.aforgues.tock.repository;

import org.aforgues.tock.domain.Game;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class GameRepository {
    private Map<String, Game> gameMap = new HashMap<>();

    public String save(Game game) {
        UUID id = UUID.randomUUID();
        gameMap.put(id.toString(), game);
        return id.toString();
    }

    public Map<String, Game> mapByKey() {
        return gameMap;
    }

    public Game findByKey(String id) {
        return gameMap.get(id);
    }
}
