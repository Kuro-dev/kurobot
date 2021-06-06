package org.kurodev.discord.message.command.generic.console;

import net.dv8tion.jda.api.entities.MessageChannel;
import org.kurodev.discord.message.command.Preparable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * @author kuro
 **/
@SuppressWarnings("ResultOfMethodCallIgnored")
public class ConsoleCommandHandler implements Preparable {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final boolean isWindows;
    ProcessBuilder processBuilder;

    public ConsoleCommandHandler() {
        isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
    }

    private static List<String> partitionString(String string, final int partitionSize) {
        List<String> parts = new ArrayList<>();
        StringBuilder part = new StringBuilder(2000);
        final int newLine = "\n".length();
        string.lines().forEachOrdered(s -> {
            if (part.length() + s.length() + newLine >= partitionSize) {
                parts.add(part.toString());
                part.delete(0, part.length());
            }
            part.append(s).append("\n");
        });
        parts.add(part.toString());
        return parts;
    }

    @Override
    public void prepare() {
        processBuilder = new ProcessBuilder();
        processBuilder.directory(Paths.get("./").toFile());
    }

    public void handle(String command, MessageChannel channel) {
        String formattedCommand = command.replaceAll("\n|\r", "&&");
        if (isWindows) {
            processBuilder.command("cmd.exe", "/c", formattedCommand);
        } else {
            processBuilder.command("sh", "-c", formattedCommand);
        }

        try {
            StringBuilder response = new StringBuilder();
            Process process = this.processBuilder.start();
            MyStreamReader gobbler = new MyStreamReader(process.getInputStream(), response);
            Executors.newSingleThreadExecutor().submit(gobbler);
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                response.append("Unknown or faulty command, exit code: ").append(exitCode);
            }
            sendResponse(channel, response.toString(), exitCode);
        } catch (IOException | InterruptedException e) {
            channel.sendMessage("Something went wrong, please check logs").queue();
            logger.error("Failed to build process", e);
        }
    }

    private void sendResponse(MessageChannel channel, String response, int exitCode) {
        List<String> brokenDownResponse = partitionString(response, 1990);
        for (String string : brokenDownResponse) {
            channel.sendMessage("```\n").append(string).append("```").queue();
        }
        channel.sendMessage("ExitCode: ").append(String.valueOf(exitCode)).append("\n").queue();
    }

}

