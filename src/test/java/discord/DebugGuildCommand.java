package discord;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.command.argument.Argument;
import org.kurodev.discord.command.guild.CommandArgument;
import org.kurodev.discord.command.guild.GuildCommand;

import java.io.IOException;

/**
 * @author kuro
 **/
public class DebugGuildCommand extends GuildCommand {
    @CommandArgument(meaning = "This is a debug flag for debugging purposes")
    private final String attribute = "--debug";
    @CommandArgument(meaning = "used to set the value of some parameter")
    private final String otherAttribute = "-setName";

    public DebugGuildCommand() {
        super("test");
    }

    @Override
    public String getDescription() {
        return "This is just my command D:";
    }

    @Override
    public void execute(TextChannel channel, Argument args, @NotNull GuildMessageReceivedEvent event) throws IOException {

    }
}
