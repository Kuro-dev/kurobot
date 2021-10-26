package org.kurodev.discord.message.command;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.discord.message.command.enums.CommandState;
import org.kurodev.discord.message.command.enums.CommandType;

import java.io.IOException;

/**
 * @author kuro
 **/
public interface Command extends Preparable {
    String IDENTIFIER = "!k";


    default void prepare() {

    }

    default void onShutdown() throws Exception {

    }

    default boolean check(String command, MessageReceivedEvent event) {
        return isFunctioning() && getCommand().equalsIgnoreCase(command);
    }

    void execute(MessageChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) throws IOException;

    default boolean needsAdmin() {
        return false;
    }

    String getCommand();

    default boolean isListed() {
        return true;
    }

    String getDescription();

    default boolean supportsMention() {
        return false;
    }

    default boolean invokerIsAdmin(PrivateMessageReceivedEvent event) {
        return invokerIsAdmin(event.getAuthor());
    }

    default boolean invokerIsAdmin(MessageReceivedEvent event) {
        return invokerIsAdmin(event.getAuthor());
    }

    default boolean invokerIsAdmin(User user) {
        return Main.ADMINS.isAdmin(user);
    }

    Options getOptions();

    default boolean hasReactAction() {
        return this instanceof Reactable;
    }

    /**
     * @deprecated replaced by {@link #getState()} and {@link #isOnline()}
     */
    @Deprecated(forRemoval = true)
    boolean isFunctioning();

    CommandState getState();

    default boolean isOnline() {
        return getState() == CommandState.ONLINE;
    }

    CommandType getType();

    String getArgumentsAsString();

    default boolean supportsChannel(ChannelType type) {
        return getType().supports(type);
    }
}
