package org.kurodev.discord.command.guild;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;

public interface Reactable {
    void onReact(Message reactedMessage, GuildMessageReactionAddEvent event);

    void onReact(Message reactedMessage, GuildMessageReactionRemoveEvent event);
}
