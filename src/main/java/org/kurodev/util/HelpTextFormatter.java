package org.kurodev.util;

import org.kurodev.command.Command;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author kuro
 **/
public class HelpTextFormatter {
    private static final int PUFFER = 5;

    public static String format(List<? extends Command> commands) {
        final int pufferLength = commands.stream().mapToInt(value -> value.getCommand().length()).max().orElse(-1) + PUFFER;
        final StringBuilder out = new StringBuilder("```List of commands:\n");
        final String delimiter = "-".repeat(pufferLength - PUFFER);

        out.append(delimiter).append("Commands").append(delimiter).append("\n");
        commands.stream().filter(command -> !command.needsAdmin()).forEach(command -> {
            String string = command.getCommand();
            out.append(string).append(createPuffer(string, pufferLength)).append("- ").append(command.getDescription()).append("\n");
        });

        out.append(delimiter).append("Admin Commands").append(delimiter).append("\n");
        commands.stream().filter(Command::needsAdmin).forEach(command -> {
            String string = command.getCommand();
            out.append(string).append(createPuffer(string, pufferLength)).append("- ").append(command.getDescription()).append("\n");
        });

        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(out.toString().getBytes(StandardCharsets.UTF_8))));
        int length = reader.lines().mapToInt(String::length).max().orElse(-1);
        out.append("-".repeat(length)).append("\n");
        out.append("Please remember to use \"!k *command*\" to execute the command.\n").append("```");
        return out.toString();
    }

    private static String createPuffer(String str, int length) {
        int diff = length - str.length();
        return " ".repeat(diff);
    }
}
