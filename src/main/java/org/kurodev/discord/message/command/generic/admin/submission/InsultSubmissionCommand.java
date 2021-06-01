package org.kurodev.discord.message.command.generic.admin.submission;

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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author kuro
 **/
@SuppressWarnings("ResultOfMethodCallIgnored")
public class InsultSubmissionCommand extends SubmissionCommand {
    private final Pattern pattern = Pattern.compile("\".+\"");
    private final TextSampleHandler handler;

    public InsultSubmissionCommand(TextSampleHandler handler) {
        this(Paths.get(Main.SETTINGS.getSetting(Setting.INSULT_SUBMISSIONS)), handler);
    }

    /**
     * @param path    Destination path for every submission of this type
     * @param handler The text samples file handler
     */
    public InsultSubmissionCommand(Path path, TextSampleHandler handler) {
        super("Insult", path);
        this.handler = handler;
    }


    @Override
    public void execute(MessageChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) throws IOException {
        final String content = event.getMessage().getContentDisplay();
        final Matcher match = pattern.matcher(content);
        final String author = event.getAuthor().getAsTag();
        Path path = this.path.resolve(author + "-submissions.txt");
        channel.sendTyping().queue();
        MessageAction msg = channel.sendMessage("Processing:\n");
        if (match.find()) {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            String insult = match.group(0);
            msg.append("Thank you for your submission\n");
            if (checkIfInsultExists(insult)) {
                msg.append("It appears that this insult is either already existing or has been submitted before :)").queue();
                return;
            }
            OutputStream out = Files.newOutputStream(path, StandardOpenOption.APPEND);
            out.write((insult + System.lineSeparator()).replaceAll("\"", "").getBytes(StandardCharsets.UTF_8));
            out.close();
            msg.append("Successfully submitted insult for review :)\nHopefully it will be added!");
        } else {
            msg.append("Please make sure to type your insult in \"\" for me to recognize it :)");
        }
        //actually sending the message now
        msg.queue();
    }

    private boolean checkIfInsultExists(String submission) {
        final String insult = submission.replaceAll("(\n|\r|\")", "");
        for (String handlerInsult : handler.getSamples()) {
            if (handlerInsult.matches(insult)) {
                return true;
            }
        }
        try {
            return Files.walk(this.path).anyMatch(path -> {
                if (Files.isRegularFile(path)) {
                    try {
                        for (String line : Files.readAllLines(path)) {
                            if (insult.matches(line)) {
                                return true;
                            }
                        }
                        return false;

                    } catch (IOException e) {
                        logger.error("Something went wrong when reading file " + path, e);
                        return false;
                    }
                }
                return false;
            });
        } catch (IOException e) {
            logger.error("Something went wrong when checking insult submissions");
            return false;
        }

    }

    @Override
    public String getDescription() {
        return "usage: Type your insult in \"your insult here\"";
    }

}
