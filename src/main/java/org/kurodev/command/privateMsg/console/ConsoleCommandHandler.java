package org.kurodev.command.privateMsg.console;

import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.kurodev.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

/**
 * @author kuro
 **/
@SuppressWarnings("ResultOfMethodCallIgnored")
public class ConsoleCommandHandler implements Command {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final boolean isWindows;
    private TimedConsole console;
    ProcessBuilder processBuilder;

    public ConsoleCommandHandler() {
        isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
    }

    @Override
    public void prepare() {
        processBuilder = new ProcessBuilder();
        processBuilder.directory(Paths.get("./").toFile());
        console = new TimedConsole(processBuilder);
    }

    public void handle(String command, PrivateChannel channel, PrivateMessageReceivedEvent event) {
        if (isWindows) {
            processBuilder.command("cmd.exe", "/c", command);
        } else {
            processBuilder.command("sh", "-c", command);
        }
        try {
            MessageAction msg = channel.sendMessage("```");
            Process process = this.processBuilder.start();
            MyStreamReader gobbler = new MyStreamReader(process.getInputStream(), msg);
            Executors.newSingleThreadExecutor().submit(gobbler);
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                msg.append("Unknown or faulty command");
            }
            msg.append("\n```ExitCode: ").append(String.valueOf(exitCode)).append("\n").queue();
        } catch (IOException | InterruptedException e) {
            channel.sendMessage("Something went wrong, please check logs").queue();
            logger.error("Failed to build process", e);
        }
    }

    @Override
    public String getCommand() {
        return "";
    }

    @Override
    public boolean needsAdmin() {
        return true;
    }
}

