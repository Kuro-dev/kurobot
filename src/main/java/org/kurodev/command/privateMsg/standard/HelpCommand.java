package org.kurodev.command.privateMsg.standard;

import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;
import org.kurodev.command.Command;
import org.kurodev.command.privateMsg.PrivateCommand;
import org.kurodev.util.HelpTextFormatter;

import java.io.IOException;
import java.util.List;

/**
 * @author kuro
 **/
@SuppressWarnings("ResultOfMethodCallIgnored")
public class HelpCommand extends PrivateCommand {
    private final List<? extends Command> commands;
    private String response;

    public HelpCommand(List<? extends Command> commands) {
        super("Help");
        this.commands = commands;
    }

    @Override
    public void prepare() throws Exception {
        response = HelpTextFormatter.format(commands);
    }

    @Override
    public String getDescription() {
        return "displays help";
    }

    //TODO add formatting using ``` text ``` formatting and make it display a table
    @Override
    public void execute(PrivateChannel channel, String[] args, @NotNull PrivateMessageReceivedEvent event) throws IOException {
        channel.sendTyping().complete();
        MessageAction msg = channel.sendMessage(response);
        msg.append("-------------------\n");
        msg.append("Please remember to use \"!k *command*\" to execute the command.").queue();
    }
}
