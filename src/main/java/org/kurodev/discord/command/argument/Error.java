package org.kurodev.discord.command.argument;

public class Error {


    final ErrorCode code;
    final String reason;
    final String cause;

    private Error(ErrorCode code, String reason, String cause) {
        this.code = code;
        this.reason = reason;
        this.cause = cause;
    }

    Error(ErrorCode code, String cause) {
        this.code = code;
        this.reason = code.reason;
        this.cause = cause;
    }

    public ErrorCode getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    public String getCause() {
        return cause;
    }
}
