package org.kurodev.discord.util.information;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.kurodev.discord.message.command.Command;
import org.kurodev.discord.message.command.enums.CommandState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandInformation {
    private final String command;
    private final String desc;
    private final CommandState state;
    private final boolean isListed;
    private final boolean needsAdmin;
    private final boolean hasReactAction;
    private final List<CommandArg> args;

    public CommandInformation(Command command) {
        this.command = command.getCommand();
        state = command.getState();
        args = convertArgs(command.getOptions());
        desc = command.getDescription();
        isListed = command.isListed();
        needsAdmin = command.needsAdmin();
        hasReactAction = command.hasReactAction();
    }

    public static List<CommandInformation> of(List<Command> commands) {
        List<CommandInformation> out = new ArrayList<>();
        for (Command command : commands) {
            out.add(new CommandInformation(command));
        }
        return out;
    }

    private List<CommandArg> convertArgs(Options args) {
        Collection<Option> arguments = args.getOptions();
        List<CommandArg> out = new ArrayList<>(arguments.size());
        for (Option option : arguments) {
            out.add(new CommandArg(option));
        }
        return out;
    }

    public String getCommand() {
        return command;
    }

    public CommandState getState() {
        return state;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isListed() {
        return isListed;
    }

    public boolean isNeedsAdmin() {
        return needsAdmin;
    }

    public boolean isHasReactAction() {
        return hasReactAction;
    }

    public List<CommandArg> getArgs() {
        return args;
    }
}
