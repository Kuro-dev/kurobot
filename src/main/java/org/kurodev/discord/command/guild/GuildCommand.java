package org.kurodev.discord.command.guild;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.command.argument.Argument;
import org.kurodev.discord.command.interfaces.Command;
import org.kurodev.discord.command.interfaces.Reactable;
import org.kurodev.discord.command.quest.Quest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.EnumSet;

/**
 * @author kuro
 **/
public abstract class GuildCommand implements Command {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final EnumSet<Permission> neededPermissions = EnumSet.noneOf(Permission.class);
    private final String command;
    private final Options args = new Options();

    public GuildCommand(String command, Permission... neededPermissions) {
        this.command = command;
        if (neededPermissions.length > 0)
            this.neededPermissions.addAll(Arrays.asList(neededPermissions));
    }

    public Options getArgs() {
        return args;
    }

    @Override
    public final void prepare() throws Exception {
        prepare(args);
    }

    protected void prepare(Options args) throws Exception {

    }

    public final String getCommand() {
        return command;
    }

    public boolean check(String command, @NotNull GuildMessageReceivedEvent event) {
        return this.command.equalsIgnoreCase(command);
    }

    /**
     * @return a list of permissions that are missing for this command.
     */
    public final Permission[] checkPermissions(GuildMessageReceivedEvent event) {
        Guild guild = event.getGuild();
        TextChannel channel = event.getChannel();
        return neededPermissions.stream()
                .filter(neededPermission ->
                        guild.getSelfMember().getPermissions(channel).contains(neededPermission))
                .toArray(Permission[]::new);
    }

    /**
     * invoking this will register the quest and hook it into the message listener directly to be able to respond to any
     * other message the registered user types. Quest will solely respond to the one that triggered it.
     * <p>Use case examples: an AI dungeon based command</p>
     *
     * @param event The event to base this off. Will store channel and user data to be able to recognize the invoker in
     *              the future of the quest.
     * @param q     The quest to register.
     */
    protected final void registerQuest(GuildMessageReceivedEvent event, Quest q) {
        GuildCommandHandler.QUESTS.register(event, q);
    }

    public final String getArgumentsAsString() {
        if (args.getOptions().isEmpty()) {
            return "There are no arguments for " + command;
        }
        final HelpFormatter formatter = new HelpFormatter();
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final String syntax = Command.IDENTIFIER + " " + command + " -argument";
        try (PrintWriter pw = new PrintWriter(baos, true, StandardCharsets.UTF_8)) {
            formatter.printHelp(pw, 200, syntax,
                    "All arguments:", args, 2, 5, "");
        }
        return baos.toString(StandardCharsets.UTF_8);
    }

    /**
     * Final removal in 1.8.0
     *
     * @deprecated Replaced by {@link #execute(TextChannel, CommandLine, GuildMessageReceivedEvent)}
     */
    @Deprecated(forRemoval = true, since = "1.7.0")
    public void execute(TextChannel channel, Argument args, @NotNull GuildMessageReceivedEvent event) throws IOException {

    }

    public abstract void execute(TextChannel channel, CommandLine args, @NotNull GuildMessageReceivedEvent event) throws IOException;


    protected boolean botHasPermission(@NotNull GuildMessageReceivedEvent event) {
        return event.getGuild().getSelfMember().hasPermission(neededPermissions);
    }

    @Override
    public String toString() {
        return "GuildCommand{" +
                "command='" + command + '\'' +
                '}';
    }

    public final boolean hasReactAction() {
        return this instanceof Reactable;
    }
}
