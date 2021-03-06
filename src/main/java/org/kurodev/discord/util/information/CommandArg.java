package org.kurodev.discord.util.information;

import org.apache.commons.cli.Option;

public class CommandArg {
    private final String opt, longOpt, desc;
    //-1 means "no args" -2 means "unlimited args" anything else means "this many args"\\
    private final int numberOfArgs;

    public CommandArg(Option arg) {
        desc = arg.getDescription();
        opt = "-" + arg.getOpt();
        if (arg.getLongOpt() != null && !arg.getLongOpt().equals("null"))
            longOpt = "--" + arg.getLongOpt();
        else
            longOpt = null;
        numberOfArgs = arg.getArgs();
    }

    public String getOpt() {
        return opt;
    }

    public String getLongOpt() {
        return longOpt;
    }

    public String getDesc() {
        return desc;
    }

    public int getNumberOfArgs() {
        return numberOfArgs;
    }
}
