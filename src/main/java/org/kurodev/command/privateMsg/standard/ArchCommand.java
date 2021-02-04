package org.kurodev.command.privateMsg.standard;

import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.command.privateMsg.PrivateCommand;

import java.io.IOException;

/**
 * @author kuro
 **/
public class ArchCommand extends PrivateCommand {
    public ArchCommand() {
        super("getArch");
    }

    @Override
    public void execute(PrivateChannel channel, String[] args, @NotNull PrivateMessageReceivedEvent event) throws IOException {
        channel.sendMessage(System.getProperty("os.name")).queue();
    }
}
