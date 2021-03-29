package discord.util;

import org.kurodev.discord.command.argument.ArgEnum;

public enum TestArgs implements ArgEnum {
    BOOL_ARG("test1"),
    NOT_PRESENT_ARG("test2"),
    PARAM_ARG("test3"),
    NORMAL_ARG("test6"),
    ARG_WITH_VALUE_SAME_AS_PARAM("value");

    private final String name;

    TestArgs(String name) {
        this.name = name;
    }

    @Override
    public String getArgumentName() {
        return name;
    }
}
