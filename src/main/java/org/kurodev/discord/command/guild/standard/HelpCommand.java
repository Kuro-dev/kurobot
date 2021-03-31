package org.kurodev.discord.command.guild.standard;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.command.guild.GuildCommand;
import org.kurodev.discord.command.interfaces.Command;
import org.kurodev.discord.util.HelpTextFormatter;

import java.util.List;

/**
 * @author kuro
 **/
public class HelpCommand extends GuildCommand {
    private static final String SHOW_ALL = "--all";
    private static final String SHOW_ADMIN_COMMANDS = "--admin";
    private final List<? extends Command> commands;

    public HelpCommand(List<? extends Command> commands) {
        super("Help");
        this.commands = commands;
    }

    @Override
    protected void prepare(Options args) throws Exception {
        args.addOption("a", "all", false, "shows all command, including hidden ones");
        args.addOption("admin", "shows developer commands");
        args.addOption("c", "command", true, "Shows information about the given command.");
    }

    @Override
    public String getDescription() {
        return "Lists all available commands. use !k help -c *command* for additional info";
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void execute(TextChannel channel, CommandLine args, @NotNull GuildMessageReceivedEvent event) {
        boolean isAdminInvoked = invokerIsAdmin(event);
        if (args.hasOption("c")) {
            Command com = find(args.getOptionValue("c"));
            if (com != null) {
                MessageAction action = channel.sendMessage(com.getCommand()).append(" - `").append(com.getDescription()).append("`\n");
                action.append("Supports @mentions: ").append(String.valueOf(com.supportsMention()));
                if (com instanceof GuildCommand) {
                    GuildCommand guildCommand = (GuildCommand) com;
                    String possibleArgs = guildCommand.getArgumentsAsString();
                    if (!possibleArgs.isBlank())
                        action.append("\nArguments:\n```\n").append(possibleArgs).append("\n```");
                }
                action.queue();
            } else {
                channel.sendMessage("Command unknown: ").append(args.getOptionValue("c")).queue();
            }
            return;
        }
        boolean showUnlisted = args.hasOption("a");
        boolean showAdmin = args.hasOption("admin");
        if (showAdmin) {
            if (!isAdminInvoked) {
                channel.sendMessage("nice try, but you're not an admin ;)")
                        .append("\nhere are the commands you CAN use:\n").queue();
            }
        }
        channel.sendMessage(HelpTextFormatter.format(commands, isAdminInvoked && showAdmin, showUnlisted)).queue();
    }

    private Command find(String name) {
        return commands.stream().filter(command -> command.getCommand().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
