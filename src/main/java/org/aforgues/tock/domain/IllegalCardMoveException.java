package org.aforgues.tock.domain;

public class IllegalCardMoveException extends RuntimeException {
    public IllegalCardMoveException(String message) {
        super(message);
    }
}
