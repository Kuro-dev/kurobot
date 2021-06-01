package org.kurodev.discord.message.command.generic.admin.force;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.apache.commons.cli.CommandLine;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.config.Setting;
import org.kurodev.discord.util.handlers.TextSampleHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author kuro
 **/
public class ForceAddInsultCommand extends ForceAddFileCommand {
    private final TextSampleHandler insultHandler;
    private final Pattern pattern = Pattern.compile("\".+\"");

    public ForceAddInsultCommand(TextSampleHandler insultHandler) {
        super("Insult", Paths.get(Main.SETTINGS.getSetting(Setting.INSULT_FILE)));
        this.insultHandler = insultHandler;
    }

    private boolean checkIfInsultExists(String submission) {
        final String insult = submission.replaceAll("(\n|\r|\")", "");
        for (String handlerInsult : insultHandler.getSamples()) {
            if (handlerInsult.matches(insult)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "used to add an insult into the official database";
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void execute(MessageChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) throws IOException {
        final String content = event.getMessage().getContentDisplay();
        final Matcher match = pattern.matcher(content);
        channel.sendTyping().queue();
        MessageAction msg = channel.sendMessage("Processing:\n");
        if (match.find()) {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            String insult = match.group(0);
            if (checkIfInsultExists(insult)) {
                msg.append("It appears that this insult is either already existing :)").queue();
                return;
            }
            OutputStream out = Files.newOutputStream(path, StandardOpenOption.APPEND);
            out.write((insult + System.lineSeparator()).replaceAll("\"", "").getBytes(StandardCharsets.UTF_8));
            out.close();
            msg.append("Insult added\n");
        } else {
            msg.append("Please make sure to type your insult in \"\" for me to recognize it :)");
        }
        //actually sending the message now
        msg.queue();
    }
}
