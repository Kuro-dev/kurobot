package org.kurodev.discord.command.guild;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.command.Command;
import org.kurodev.discord.command.argument.ArgInfo;
import org.kurodev.discord.command.argument.Argument;
import org.kurodev.discord.command.quest.Quest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author kuro
 **/
public abstract class GuildCommand implements Command {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final Permission[] neededPermissions;
    protected final List<ArgInfo> argInformation;
    private final String command;

    public GuildCommand(String command, Permission... neededPermissions) {
        this.command = command;
        this.neededPermissions = neededPermissions;
        argInformation = getArguments();
    }

    public String getCommand() {
        return command;
    }

    public boolean check(String command, @NotNull GuildMessageReceivedEvent event) {
        return this.command.equalsIgnoreCase(command);
    }

    private List<ArgInfo> getArguments() {
        List<ArgInfo> out = new ArrayList<>();
        for (Field declaredField : getClass().getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(CommandArgument.class)) {
                try {
                    declaredField.setAccessible(true);
                    String name = declaredField.get(this).toString();
                    CommandArgument argument = declaredField.getAnnotation(CommandArgument.class);
                    boolean mandatory = argument.mandatory();
                    String meaning = argument.meaning();
                    boolean requireAdmin = argument.requireAdmin();
                    out.add(new ArgInfo(name, mandatory, requireAdmin, meaning));
                    declaredField.setAccessible(false);
                } catch (IllegalAccessException e) {
                    //should never be thrown
                    e.printStackTrace();
                }
            }
        }
        return out;
    }

    protected final void registerQuest(GuildMessageReceivedEvent event, Quest q) {
        GuildCommandHandler.QUESTS.register(event, q);
    }

    public String getArgumentsAsString() {
        StringBuilder builder = new StringBuilder();
        if (!argInformation.isEmpty()) {
            for (ArgInfo argument : argInformation) {
                String name = argument.getName();
                String mandatory = argument.isMandatory() ? " (Mandatory)" : "";
                builder.append(name).append(mandatory).append(" = ").append(argument.getMeaning()).append("\n");
            }
        }
        return builder.toString();
    }

    public abstract void execute(TextChannel channel, Argument args, @NotNull GuildMessageReceivedEvent event) throws IOException;

    protected boolean botHasPermission(@NotNull GuildMessageReceivedEvent event) {
        return event.getGuild().getSelfMember().hasPermission(neededPermissions);
    }

    public List<ArgInfo> getArgInformation() {
        return Collections.unmodifiableList(argInformation);
    }
}
