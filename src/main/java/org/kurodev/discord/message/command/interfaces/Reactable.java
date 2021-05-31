package org.kurodev.discord.message.command.interfaces;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.message.CommandHandler;

/**
 * If the command can support reactions, simply make it implement this interface, the rest will happen automatically.
 * (don't forget to add your command to {@link CommandHandler#prepare(Runnable)}
 */
public interface Reactable {
    void onReact(Message reactedMessage, @NotNull MessageReactionRemoveEvent event);

    void onReact(Message reactedMessage, @NotNull MessageReactionAddEvent event);
}
