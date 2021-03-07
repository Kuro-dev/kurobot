package org.kurodev.discord.command.argument;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kuro
 **/
public class Argument {
    private final List<String> otherArgs;
    private final List<Option> paramArg;
    private final List<String> booleanArg;
    private final List<Error> errors;

    private Argument(List<String> argsList, List<Option> optsList, List<String> doubleOptsList, List<Error> errors) {
        this.otherArgs = argsList;
        this.paramArg = optsList;
        this.booleanArg = doubleOptsList;
        this.errors = errors;
    }

    public static Argument parse(String[] args) {
        final List<String> argsList = new ArrayList<>();
        final List<Option> optsList = new ArrayList<>();
        final List<String> doubleOptsList = new ArrayList<>();
        final List<Error> errors = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].length() == 0) {
                continue;
            }
            if (args[i].charAt(0) == '-') {
                if (args[i].length() < 2) {
                    errors.add(new Error(ErrorCode.ARGUMENT_TOO_SHORT, args[i]));
                } else if (args[i].charAt(1) == '-') {
                    if (args[i].length() < 3) {
                        errors.add(new Error(ErrorCode.ARGUMENT_TOO_SHORT, args[i]));
                    } else {
                        // --opt
                        doubleOptsList.add(args[i].substring(2));
                    }
                } else {
                    if (args.length - 1 == i || args[i + 1].startsWith("-"))
                        errors.add(new Error(ErrorCode.OPTION_SYNTAX_ERROR, args[i]));
                    else {
                        // -opt
                        optsList.add(new Option(args[i], args[i + 1]));
                        i++;
                    }
                }
            } else {// arg
                argsList.add(args[i]);
            }
        }
        return new Argument(argsList, optsList, doubleOptsList, errors);
    }

    public String getParam(String param) {
        final String flag;
        if (param.startsWith("-")) {
            flag = param;
        } else {
            flag = "-" + param;
        }
        return paramArg.stream().filter(option -> option.flag.equals(flag)).findFirst().map(option -> option.opt).orElse(null);
    }

    public boolean getOpt(String param) {
        final String flag;
        if (param.startsWith("--")) {
            flag = param.substring(2);
        } else {
            flag = param;
        }
        return booleanArg.contains(flag);
    }

    public List<String> getOtherArgs() {
        return otherArgs;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public String getErrorsAsString() {
        StringBuilder out = new StringBuilder();
        for (Error error : errors) {
            out.append(String.format("```Error code: %d (%s):\nReason: %s\ncaused by argument:\"%s\"\n```", error.code.ordinal(), error.code.name(), error.code.reason, error.cause));
        }
        return out.toString();
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }

    public boolean containsAny(List<String> conditionNames) {
        return otherArgs.stream().anyMatch(conditionNames::contains);
    }

    public boolean hasOtherArgs() {
        return otherArgs.size() > 0;
    }

    static class Option {
        String flag, opt;

        private Option(String flag, String opt) {
            this.flag = flag;
            this.opt = opt;
        }
    }

    public static class Error {

        private final ErrorCode code;
        private final String reason;
        private final String cause;

        private Error(ErrorCode code, String reason, String cause) {
            this.code = code;
            this.reason = reason;
            this.cause = cause;
        }

        private Error(ErrorCode code, String cause) {
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
}
