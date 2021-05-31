package org.kurodev.discord.message.command.argument;
/**
 * @deprecated will soon be replaced}
 **/
@Deprecated(forRemoval = true, since = "1.7.0")
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
