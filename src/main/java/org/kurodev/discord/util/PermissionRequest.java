package org.kurodev.discord.util;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.kurodev.discord.message.quest.Quest;

import java.security.SecureRandom;

/**
 * Sends a permission request to the given user to confirm a given action.
 * Confirmation is achieved by sending the user a code in a private message
 * and having them paste this code in the channel where they invoked the given command.
 * once successful the given runnable will be executed.
 */
public class PermissionRequest extends Quest {
    private static final SecureRandom RANDOM = new SecureRandom();
    private final User userToAsk;
    private final MessageReceivedEvent event;
    private final MessageChannel channel;
    private String code;


    public PermissionRequest(MessageReceivedEvent event, Runnable onConfirm) {
        this(event.getAuthor(), event, onConfirm);
        setTitle("Permission request for user " + event.getAuthor().getAsTag());
    }

    public PermissionRequest(User userToAsk, MessageReceivedEvent event, Runnable onConfirm) {
        super();
        this.userToAsk = userToAsk;
        this.event = event;
        this.channel = event.getChannel();
        super.setOnFinished((quest, event1) -> onConfirm.run());
    }

    @Override
    public void prepare() {
        code = generateRandomCode();
        channel.sendMessage("please enter the generated code to confirm, it was sent in a private message").queue();
        String msg = "Please enter the following code: " + MarkDown.CODE.wrap(code);
        if (event.isFromGuild()) {
            userToAsk.openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(msg).queue();
            });
        } else {
            channel.sendMessage(msg).queue();
        }
    }

    private String generateRandomCode() {
        long a = RANDOM.nextLong();
        return Long.toString(a, Character.MAX_RADIX);
    }

    @Override
    protected boolean process(MessageReceivedEvent event) {
        return event.getMessage().getContentDisplay().equals(code);
    }
}
