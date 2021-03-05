package org.kurodev.command;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.kurodev.UserIDs;

import java.util.Arrays;
import java.util.List;

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

    /**
     * @param args    the args given in the command
     * @param keyword The argument to look for
     * @return <code>true</code> if the given keyword exists in the arguments. <code>false</code> otherwise
     * (case insensitive)
     * @throws NullPointerException if keyword is <code>null</code>
     */
    default boolean argsContain(String[] args, String keyword) {
        return argIndex(keyword, args) >= 0;
    }

    /**
     * @param keyword The argument to look for
     * @param args    the args given in the command
     * @return the index of the argument. -1 if not found
     * @throws IllegalArgumentException if keyword is <code>null</code>
     */
    default int argIndex(String keyword, String[] args) {
        for (int i = 0, argsLength = args.length; i < argsLength; i++) {
            if (keyword.equalsIgnoreCase(args[i])) {
                return i;
            }
        }
        return -1;
    }

    default boolean argsContain(String[] args, String[] list, boolean acceptAny) {
        return argsContain(args, Arrays.asList(list), acceptAny);
    }

    default boolean argsContain(String[] args, String[] list) {
        return argsContain(args, Arrays.asList(list), true);
    }

    default boolean argsContain(String[] args, List<String> list) {
        return argsContain(args, list, true);
    }

    /**
     * @param acceptAny if true will return true if any of the items in the list are matching
     *                  if set to false will only return true if all the list items are present in the list.
     *                  <p>default: true</>
     */
    default boolean argsContain(String[] args, List<String> list, boolean acceptAny) {
        boolean match = true;
        for (String item : list) {
            if (argsContain(args, item)) {
                match = true;
                if (acceptAny) {
                    break;
                }
            } else {
                match = false;
                if (!acceptAny) {
                    break;
                }
            }
        }
        return match;
    }


    default boolean isListed() {
        return true;
    }

    String getDescription();

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
