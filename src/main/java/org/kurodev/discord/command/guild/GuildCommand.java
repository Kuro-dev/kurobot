package org.kurodev.discord.command.guild;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.command.Command;
import org.kurodev.discord.command.argument.Argument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @author kuro
 **/
public abstract class GuildCommand implements Command {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final Permission[] neededPermissions;
    private final String command;

    public GuildCommand(String command, Permission... neededPermissions) {
        this.command = command;
        this.neededPermissions = neededPermissions;
    }

    public String getCommand() {
        return command;
    }

    public boolean check(String command, @NotNull GuildMessageReceivedEvent event) {
        return this.command.equalsIgnoreCase(command);
    }

    public String getArguments() {
        StringBuilder builder = new StringBuilder();
        for (Field declaredField : getClass().getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(CommandArgument.class)) {
                try {
                    declaredField.setAccessible(true);
                    String name = declaredField.get(this).toString();
                    String meaning = declaredField.getAnnotation(CommandArgument.class).meaning();
                    builder.append(name).append(" = ").append(meaning).append("\n");
                    declaredField.setAccessible(false);
                } catch (IllegalAccessException e) {
                    //should never be thrown
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }

    public abstract void execute(TextChannel channel, Argument args, @NotNull GuildMessageReceivedEvent event) throws IOException;

    protected boolean botHasPermission(@NotNull GuildMessageReceivedEvent event) {
        return event.getGuild().getSelfMember().hasPermission(neededPermissions);
    }
}
