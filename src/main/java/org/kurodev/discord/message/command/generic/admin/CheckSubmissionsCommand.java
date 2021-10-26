package org.kurodev.discord.message.command.generic.admin;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.apache.commons.cli.CommandLine;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.config.Setting;
import org.kurodev.discord.message.command.AutoRegister;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author kuro
 **/
@SuppressWarnings("ResultOfMethodCallIgnored")
@AutoRegister
public class CheckSubmissionsCommand extends AdminCommand {
    Path insultSubmissions, memeSubmissions;

    public CheckSubmissionsCommand() {
        super("checkSubmissions");
        insultSubmissions = Paths.get(Main.SETTINGS.getSetting(Setting.INSULT_SUBMISSIONS));
        memeSubmissions = Paths.get(Main.SETTINGS.getSetting(Setting.MEME_SUBMISSIONS));
    }


    private int readFiles(Path path) throws IOException {
        return (int) Files.walk(path).filter(Files::isRegularFile).count();
    }

    private int readInsultLines() {
        try {
            return Files.walk(insultSubmissions).filter(Files::isRegularFile).mapToInt(value -> {
                try {
                    return Files.readAllLines(value).size();
                } catch (IOException e) {
                    logger.error("Could not read file: " + value, e);
                    return 0;
                }
            }).sum();
        } catch (IOException e) {
            logger.error("failed to retrieve files from submissions", e);
            return 0;
        }
    }

    @Override
    public String getDescription() {
        return "displays information about current submissions.";
    }

    @Override
    public void execute(MessageChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) throws IOException {
        MessageAction msg = channel.sendMessage("Submission statistics:\n");
        int memeFiles = readFiles(memeSubmissions);
        int insultFiles = readFiles(insultSubmissions);
        int insultLines = readInsultLines();
        msg.append(String.format("```\nMeme Files: %d\n", memeFiles));
        msg.append(String.format("Insult Files: %d, insult lines: %d\n", insultFiles, insultLines));
        int total = memeFiles + insultFiles, absoluteTotal = total + insultLines;
        msg.append(String.format("Total Submissions: %d (%d)\n```", total, absoluteTotal));
        msg.queue();
    }
}