package org.aforgues.tock.presentation;

import lombok.Data;
import lombok.ToString;
import org.aforgues.tock.domain.Card;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ToString
@GamePlayValidatorConstraint
public class GamePlayRequest implements Serializable {
    @NotNull (message = "La référence de la partie est obligatoire")
    private String gameId;

    @NotNull (message = "Vous devez choisir une carte")
    @NotEmpty (message = "Vous devez choisir une carte")
    private String cardId;

    @NotNull (message = "Vous devez sélectionner un pion pour jouer")
    @Min(value = 1, message = "Le numéro de pion doit être 1, 2, 3 ou 4")
    @Max(value = 4, message = "Le numéro de pion doit être 1, 2, 3 ou 4")
    private Integer pawnNumber;

    @Min(value = 0, message = "La position du point cible doit être supérieure ou égale à 0")
    @Max(value = 71, message = "La position du point cible doit être inférieure ou égale à 71")
    private Integer targetPosition;

    public GamePlayRequest(String gameId) {
        this.gameId = gameId;
    }
}
