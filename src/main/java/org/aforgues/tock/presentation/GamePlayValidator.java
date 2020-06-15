package org.aforgues.tock.presentation;

import org.aforgues.tock.domain.Card;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GamePlayValidator implements ConstraintValidator<GamePlayValidatorConstraint, GamePlayRequest> {
   public void initialize(GamePlayValidatorConstraint constraint) {
   }

   public boolean isValid(GamePlayRequest gamePlayRequest, ConstraintValidatorContext context) {
      if (gamePlayRequest == null) {
         return false;
      }

      if (Card.from(gamePlayRequest.getCardId()).getCardValue() == Card.CardValue.JACK && gamePlayRequest.getTargetPosition() == null) {
         return false;
      }

      return true;
   }
}
