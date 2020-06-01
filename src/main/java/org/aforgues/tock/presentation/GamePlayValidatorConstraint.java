package org.aforgues.tock.presentation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = GamePlayValidator.class)
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface GamePlayValidatorConstraint {

    String message() default
            "La position du point cible est obligatoire avec un valet.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}