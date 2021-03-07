package org.kurodev.discord.command.argument;

/**
 * @author kuro
 **/
public enum ErrorCode {
    ARGUMENT_TOO_SHORT("The argument contains too few characters"),
    OPTION_SYNTAX_ERROR("Expected a parameter following the argument"),
    ;

    public final String reason;

    ErrorCode() {
        this("");
    }

    ErrorCode(String reason) {
        this.reason = reason;
    }
}
