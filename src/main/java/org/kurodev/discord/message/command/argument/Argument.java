package org.kurodev.discord.message.command.argument;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * @author kuro
 * @deprecated will soon be replaced}
 **/
@Deprecated(forRemoval = true, since = "1.7.0")
public class Argument {
    private final String[] argsRaw;
    private final List<String> otherArgs;
    private final List<Option> paramArg;
    private final List<String> booleanArg;
    private final List<Error> errors;

    private Argument(String[] argsRaw, List<String> argsList, List<Option> optsList, List<String> doubleOptsList, List<Error> errors) {
        this.argsRaw = argsRaw;
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
        return new Argument(args, argsList, optsList, doubleOptsList, errors);
    }

    /**
     * @param param The argument to get the parameter for
     * @return the value of the given argument or null if none was given
     */
    public String getParam(String param) {
        final String flag;
        if (param.startsWith("-")) {
            flag = param;
        } else {
            flag = "-" + param;
        }
        return paramArg.stream().filter(option -> option.flag.equals(flag)).findFirst().map(option -> option.opt).orElse(null);
    }

    /**
     * @param param The argument to get the parameter for
     * @return {@code true} if the argument has been passed {@code false} otherwise
     */
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
            out.append(String.format("```Error code: %d (%s):\nReason: %s\ncaused by argument:\"%s\"\n```",
                    error.code.ordinal(), error.getCode().name(), error.getCode().reason, error.getCause()));
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

    public String[] getArgsRaw() {
        return argsRaw;
    }

    /**
     * @param args The available arguments as EnumSet
     * @return A list of all Arguments that are present in this instance.
     *
     * @apiNote use {@link EnumSet#allOf(Class)} to create an EnumSet from an {@link Enum}
     */
    public <T extends EnumSet<? extends ArgEnum>> T checkBulk(T args) {
        T out = (T) args.clone();
        for (ArgEnum arg : args) {
            String argName = arg.getArgumentName();
            if (!getOpt(argName) && (getParam(argName) == null) && !otherArgs.contains(argName)) {
                out.remove(arg);
            }
        }
        return out;
    }
}