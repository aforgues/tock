package org.aforgues.tock.presentation;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ToString
public class GamePassRequest implements Serializable {
    @NotNull(message = "La référence de la partie est obligatoire")
    private String gameId;

    public GamePassRequest(String gameId) {
        this.gameId = gameId;
    }
}

