package org.kurodev.discord.util;

import net.dv8tion.jda.api.entities.ChannelType;
import org.kurodev.discord.message.command.Command;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author kuro
 **/
public class HelpTextFormatter {
    private static final int BUFFER = 5;

    public static String format(List<Command> commands, boolean invokerIsAdmin, boolean showAll, ChannelType type) {
        final int pufferLength = commands.stream().mapToInt(value -> value.getCommand().length()).max().orElse(-1) + BUFFER;
        final StringBuilder out = new StringBuilder("List of commands:\n");
        final String delimiter = "-".repeat(pufferLength - BUFFER);

        out.append(delimiter).append("Commands").append(delimiter).append("\n");
        commands.stream().filter(command -> !command.needsAdmin() && ((showAll || command.isListed()) && command.supportsChannel(type))).
                forEach(command -> {
                    String name = command.getCommand();
                    out.append(name).append(createPuffer(name, pufferLength)).append("- ").append(command.getDescription()).append("\n");
                });
        if (invokerIsAdmin && type == ChannelType.PRIVATE) {
            out.append(delimiter).append("Admin Commands").append(delimiter).append("\n");
            commands.stream().filter(Command::needsAdmin).forEach(command -> {
                String string = command.getCommand();
                out.append(string).append(createPuffer(string, pufferLength)).append("- ").append(command.getDescription()).append("\n");
            });
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(out.toString().getBytes(StandardCharsets.UTF_8))));
        int length = reader.lines().mapToInt(String::length).max().orElse(-1);
        out.append("-".repeat(length)).append("\n");
        out.append("Please remember to use \"!k *command*\" to execute the command.\n");
        return MarkDown.CODE_BLOCK.wrap(out.toString());
    }

    private static String createPuffer(String commandName, int length) {
        int diff = length - commandName.length();
        return " ".repeat(diff);
    }
}
