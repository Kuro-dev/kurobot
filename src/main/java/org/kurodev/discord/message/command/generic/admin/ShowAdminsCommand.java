package org.kurodev.discord.message.command.generic.admin;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.discord.DiscordBot;
import org.kurodev.discord.message.command.AutoRegister;
import org.kurodev.discord.util.Admins;

import java.io.IOException;
import java.util.regex.Pattern;
@AutoRegister
public class ShowAdminsCommand extends AdminCommand {
    private static final Pattern isNumber = Pattern.compile("\\d+");
    private final Admins admins = Main.ADMINS;

    public ShowAdminsCommand() {
        super("admins");
    }

    @Override
    protected void prepare(Options args) throws Exception {
        args.addOption("a", "add", true, "userID as argument");
        args.addOption("rm", "remove", true, "userID as argument");
    }

    //TODO
    // add ability to use discord tag for removing and adding
    // remove code duplication.
    @Override
    public void execute(MessageChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) throws IOException {
        if (args.hasOption("a")) {
            String value = args.getOptionValue("a");
            if (isNumber.matcher(value).matches()) {
                DiscordBot.getJda().retrieveUserById(value).queue(user -> {
                    if (admins.isAdmin(user)) {
                        String msg = String.format("user \"%s\" is already admin", user.getName());
                        channel.sendMessage(msg).queue();
                    } else {
                        String msg;
                        try {
                            admins.getAll().add(user.getIdLong());
                            admins.save();
                            msg = String.format("added \"%s\" as admin", user.getName());
                            logger.info(msg);
                        } catch (IOException e) {
                            msg = "Something went wrong when saving admins: " + e.getMessage();
                            logger.error(msg, e);
                        }
                        channel.sendMessage(msg).queue();
                    }
                }, throwable -> channel.sendMessage("Something went wrong: " + throwable.getMessage()).queue());
            } else {
                channel.sendMessage("Value must be numeric").queue();
            }
        } else if (args.hasOption("rm")) {
            String value = args.getOptionValue("rm");
            if (isNumber.matcher(value).matches()) {
                DiscordBot.getJda().retrieveUserById(value).queue(user -> {
                    if (admins.isAdmin(user)) {
                        admins.getAll().remove(user.getIdLong());
                        String msg = String.format("removed admin from \"%s\"", user.getName());
                        logger.info(msg);
                        channel.sendMessage(msg).queue();
                    } else {
                        String msg = String.format("user \"%s\" does not have admin privileges", user.getName());
                        channel.sendMessage(msg).queue();
                    }
                });
                admins.save();
            } else {
                channel.sendMessage("Value must be numeric").queue();
            }
        } else {
            Thread thread = new Thread(() -> {
                StringBuilder builder = new StringBuilder();
                for (Long id : admins.getAll()) {
                    User user = DiscordBot.getJda().retrieveUserById(id).complete();
                    builder.append(user.getAsTag()).append("\n");
                }
                channel.sendMessage(builder.toString()).queue();
            }, "Send admins thread");
            thread.setDaemon(true);
            thread.start();
        }
    }

    @Override
    public String getDescription() {
        return "Adds an admin to the admin list";
    }
}
