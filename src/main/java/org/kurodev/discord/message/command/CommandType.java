package org.kurodev.discord.message.command;

import net.dv8tion.jda.api.entities.ChannelType;

public enum CommandType {
    GUILD(ChannelType.TEXT),
    ADMIN(ChannelType.PRIVATE),
    GENERIC(ChannelType.TEXT, ChannelType.PRIVATE),
    ;

    private final ChannelType[] channelTypes;

    CommandType(ChannelType... types) {
        this.channelTypes = types;
    }

    public boolean supports(ChannelType channelType) {
        for (ChannelType supportedType : channelTypes) {
            if (supportedType == channelType) {
                return true;
            }
        }
        return false;
    }
}
