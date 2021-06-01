package org.kurodev.discord.message.command.generic;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.config.Setting;
import org.kurodev.discord.message.CommandHandler;
import org.kurodev.discord.message.command.Command;
import org.kurodev.discord.message.command.CommandType;
import org.kurodev.discord.message.quest.Quest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.EnumSet;

/**
 * @author kuro
 **/
public abstract class GenericCommand implements Command {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final EnumSet<Permission> neededPermissions = EnumSet.noneOf(Permission.class);
    private final Options args = new Options();
    private final String command;
    private final CommandType type;
    private boolean functioning = false;

    public GenericCommand(String command, Permission... neededPermissions) {
        this(command, CommandType.GENERIC, neededPermissions);
    }

    public GenericCommand(String command, CommandType type, Permission... neededPermissions) {
        this.command = command;
        this.type = type;
        if (neededPermissions.length > 0)
            this.neededPermissions.addAll(Arrays.asList(neededPermissions));
    }

    @Override
    public String toString() {
        return "GenericCommand{" +
                "command='" + command + '\'' +
                ", type=" + type +
                ", functioning=" + functioning +
                '}';
    }

    @Override
    public final void prepare() throws Exception {
        prepare(args);
        functioning = true;
    }

    protected void prepare(Options args) throws Exception {

    }

    protected String getSetting(Setting setting) {
        return Main.SETTINGS.getSetting(setting);
    }

    protected boolean getSettingBool(Setting setting) {
        return Main.SETTINGS.getSettingBool(setting);
    }

    public final Options getArgs() {
        return args;
    }


    public final String getCommand() {
        return command;
    }

    /**
     * @return an array of permissions that are missing for this command.
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
    protected final void registerQuest(MessageReceivedEvent event, Quest q) {
        CommandHandler.QUESTS.register(event, q);
    }
@Override
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


    protected boolean botHasPermission(@NotNull MessageReceivedEvent event) {
        return event.getGuild().getSelfMember().hasPermission(neededPermissions);
    }


    public boolean isFunctioning() {
        return functioning;
    }

    public void setFunctioning(boolean functioning) {
        this.functioning = functioning;
    }

    @Override
    public CommandType getType() {
        return type;
    }
}
