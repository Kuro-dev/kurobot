package org.kurodev.discord.message.command;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.message.command.generic.GenericCommand;
import org.kurodev.discord.util.PermissionRequest;

import java.io.IOException;
@AutoRegister
public class TestCommand extends GenericCommand {
    public TestCommand() {
        super("test");
    }

    @Override
    public boolean isListed() {
        return false;
    }

    @Override
    public void execute(MessageChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) throws IOException {
        var permission = new PermissionRequest(event, () -> channel.sendMessage("Confirmation successful").queue());
        registerQuest(event, permission);
    }

    @Override
    public String getDescription() {
        return null;
    }
}
