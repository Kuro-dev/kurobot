package org.kurodev.command.privateMsg;

import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.command.privateMsg.standard.ArchCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author kuro
 **/
public class PrivateCommandHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final List<PrivateCommand> commands = new ArrayList<>();
    ConsoleCommandHandler console = new ConsoleCommandHandler();

    public void prepare() {
        logger.info("initializing commands");
        commands.add(new ArchCommand());
        console.prepare();

        for (PrivateCommand command : commands) {
            try {
                command.prepare();
            } catch (Exception e) {
                logger.info("Failed to initialize command \"" + command.getCommand() + "\"", e);
                commands.remove(command);
            }
        }
        logger.info("initializing commands - DONE");
    }

    public void handle(@NotNull PrivateMessageReceivedEvent event) {
        PrivateChannel channel = event.getChannel();
        channel.sendTyping().queue();
        String message = event.getMessage().getContentDisplay();
        boolean isBotCommand = message.startsWith("!k") || message.startsWith("!K");
        if (isBotCommand) {
            String[] split = message.split(" ");
            if (split.length > 1) {
                String command = split[1];
                String[] args = Arrays.copyOfRange(split, 2, split.length);

                for (PrivateCommand com : commands) {
                    if (com.check(command, event)) {
                        try {
                            com.execute(channel, args, event);
                        } catch (IOException e) {
                            channel.sendMessage("something went wrong: " + e.getMessage()).queue();
                            logger.debug(this.getClass().getSimpleName() + "#handle() exception logged", e);
                        }
                        return;
                    }
                }
                channel.sendMessage("Command not recognized: ").append(command).queue();
            }
        } else {
            //Found out it is not a bot command
            console.handle(event.getMessage().getContentDisplay(), event.getChannel(), event);
        }
    }

}
