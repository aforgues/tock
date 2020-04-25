package org.aforgues.tock.domain;

public class IllegalPawnMoveException extends RuntimeException {
    public IllegalPawnMoveException(String message) {
        super(message);
    }
}
