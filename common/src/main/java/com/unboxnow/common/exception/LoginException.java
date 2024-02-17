package com.unboxnow.common.exception;

public class LoginException extends RuntimeException {

    public LoginException(int memberId, String reason) {
        super(String.format("object -> LoginForm, memberId -> %d, reason -> %s", memberId, reason));
    }

    public LoginException(String username, String reason) {
        super(String.format("object -> LoginForm, username -> %s, reason -> %s", username, reason));
    }
}
