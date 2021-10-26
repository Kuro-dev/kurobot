package org.kurodev.discord.message.command.generic;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.message.command.AutoRegister;
import org.kurodev.discord.util.UrlRequest;

import java.io.IOException;

@AutoRegister
public class NumberFactCommand extends GenericCommand {
    private static final String URL = "http://numbersapi.com/";
    private final UrlRequest request = new UrlRequest();

    public NumberFactCommand() {
        super("num");
    }

    @Override
    protected void prepare(Options args) throws Exception {
        args.addOption("n", "number", true, "specify a number");
        args.addOption("y", "year", false, "gives fact about a year");
        args.addOption("d", "date", false, "gives fact about a date");
        args.addOption("m", "math", false, "gives a math fact");
    }

    @Override
    public void execute(MessageChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) throws IOException {
        boolean year = args.hasOption("y");
        boolean date = args.hasOption("d");
        boolean math = args.hasOption("m");
        if (MaxOneIsTrue(year, date, math)) {
            String numberStr = args.getOptionValue("n", "random");
            String response = null;
            if (year) {
                response = request.get(URL + numberStr + "/year");
            }
            if (date) {
                response = request.get(URL + numberStr + "/date");
            }
            if (math) {
                response = request.get(URL + numberStr + "/math");
            }
            if (response == null) {
                response = request.get(URL + numberStr + "/trivia");
            }
            if (response != null)
                channel.sendMessage(response).queue();
        } else {
            channel.sendMessage("Illegal argument combination").queue();
        }
    }

    @Override
    public String getDescription() {
        return "sends a random interesting math fact";
    }

    private boolean MaxOneIsTrue(boolean... booleans) {
        int trues = 0;
        for (boolean aBoolean : booleans) {
            if (aBoolean) trues++;
        }
        return trues <= 1;
    }
}
