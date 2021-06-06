package org.kurodev.discord.message.command.generic;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.message.command.Command;
import org.kurodev.discord.util.HelpTextFormatter;

import java.util.List;

/**
 * @author kuro
 **/
public class HelpCommand extends GenericCommand {
    private final List<Command> commands;

    public HelpCommand(List<Command> commands) {
        super("Help");
        this.commands = commands;
    }

    @Override
    protected void prepare(Options args) throws Exception {
        args.addOption("all", "shows all command, including the ones for different channels ones");
        args.addOption("c", "command", true, "Shows information about the given command.");
    }

    @Override
    public String getDescription() {
        return "Lists all available commands. use !k help -c *command* for additional info";
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void execute(MessageChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) {
        boolean isAdminInvoked = invokerIsAdmin(event);
        String[] unrecognized = args.getArgs();
        if (unrecognized.length > 0) {
            for (String str : unrecognized) {
                Command com = find(str);
                if (com != null) {
                    MessageAction action = channel.sendMessage(com.getCommand()).append(" - `").append(com.getDescription()).append("`\n");
                    action.append("Supports @mentions: ").append(String.valueOf(com.supportsMention()));
                    String possibleArgs = com.getArgumentsAsString();
                    if (!possibleArgs.isBlank())
                        action.append("\nArguments:\n```\n").append(possibleArgs).append("\n```");
                    action.queue();
                } else {
                    channel.sendMessage("Command unknown: ").append(args.getOptionValue("c")).queue();
                }
                return;
            }
        }
        boolean showAll = args.hasOption("a");
        ChannelType type = event.getChannelType();
        channel.sendMessage(HelpTextFormatter.format(commands, isAdminInvoked, showAll, type)).queue();
    }

    private Command find(String name) {
        return commands.stream().filter(command -> command.getCommand().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
