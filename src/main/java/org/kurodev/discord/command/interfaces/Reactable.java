package org.kurodev.discord.command.interfaces;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import org.kurodev.discord.command.guild.GuildCommandHandler;

/**
 * If the command can support reactions, simply make it implement this interface, the rest will happen automatically.
 * (don't forget to add your command to {@link GuildCommandHandler#prepare()})
 */
public interface Reactable {
    void onReact(Message reactedMessage, GuildMessageReactionAddEvent event);

    void onReact(Message reactedMessage, GuildMessageReactionRemoveEvent event);
}
