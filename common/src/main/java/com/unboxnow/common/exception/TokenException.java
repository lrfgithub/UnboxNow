package com.unboxnow.common.exception;

import com.unboxnow.common.constant.Token;

public class TokenException extends RuntimeException {

    public TokenException(Token tokenType, String reason) {
        super(String.format("object -> token, ,type -> %s, reason -> %s",
                tokenType.getHeaderKey(),
                reason));
    }

    public TokenException() {
        super(String.format("object -> token, ,type -> %s & %s, reason -> memberId does not match",
                Token.ACCESS.getHeaderKey(),
                Token.REFRESH.getHeaderKey()));
    }
}
