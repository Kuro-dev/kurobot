package org.kurodev.discord.command;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.kurodev.discord.UserIDs;

/**
 * @author kuro
 **/
public interface Command {
    /**
     * @throws Exception if the preparation fails meaning the command will not at all be usable.
     *                   Thus will be removed from the available commands list to avoid exceptions
     */
    default void prepare() throws Exception {

    }

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

    default boolean invokerIsAdmin(GuildMessageReceivedEvent event) {
        return invokerIsAdmin(event.getAuthor());
    }

    default boolean invokerIsAdmin(User user) {
        return user.getIdLong() == (UserIDs.KURO.getId());
    }

}
