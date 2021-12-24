package org.kurodev.script;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.kurodev.discord.message.command.generic.console.MyStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.file.Path;

public class ResponseInterpreter extends MyStreamReader {
    private static final Logger logger = LoggerFactory.getLogger(ResponseInterpreter.class);
    private final MessageReceivedEvent event;
    private MessageAction response;

    public ResponseInterpreter(InputStream stream, MessageReceivedEvent event) {
        super(stream, null);
        this.event = event;
    }

    public void accept(String line) {
        MessageType type = MessageType.identify(line.substring(0, 1));
        String message = line.substring(2);
        switch (type) {
            case LOG -> logger.info(message);
            case MSG -> {
                if (response == null) {
                    response = event.getChannel().sendMessage(message);
                } else {
                    response = response.append(message);
                }
            }
            case ATTACH_FILE -> {
                Path p = Path.of(message);
                if (response == null) {
                    response = event.getChannel().sendFile(p.toFile());
                } else {
                    response = response.addFile(p.toFile());
                }
            }
            case SEND -> {
                if (response != null) {
                    response.queue();
                } else {
                    logger.warn("Tried sending an empty message");
                }
            }
        }
    }

}
