package org.kurodev.discord.message.command.interfaces;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.UserIDs;

import java.io.IOException;

/**
 * @author kuro
 **/
public interface Command {
    String IDENTIFIER = "!k";

    /**
     * Prepare everything that is necessary for this command to work here. Especially things, that can fail, like file
     * operations etc. Will be invoked once during the start of the bot. May be invoked again at a later time.
     *
     * @throws Exception if the preparation fails meaning the command will not at all be usable. Thus will be removed
     *                   from the available commands list to avoid exceptions
     * @see org.kurodev.discord.message.command.generic.admin.ReloadSettingsCommand
     */
    default void prepare() throws Exception {

    }

    default void onShutdown() throws Exception {

    }

    default boolean check(String command, MessageReceivedEvent event) {
        return isFunctioning()&& getCommand().equalsIgnoreCase(command);
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
        return user.getIdLong() == (UserIDs.KURO.getId());
    }

    Options getArgs();

    default boolean hasReactAction() {
        return this instanceof Reactable;
    }

    boolean isFunctioning();
}