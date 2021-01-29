package org.kurodev.command.submission;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;
import org.kurodev.events.InsultHandler;

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
    private final InsultHandler handler;

    public InsultSubmissionCommand(InsultHandler handler) {
        this(Paths.get("./insultSubmissions"), handler);
    }

    /**
     * @param path    Destination path for every submission of this type
     * @param handler
     */
    public InsultSubmissionCommand(Path path, InsultHandler handler) {
        super("Insult", path, false);
        this.handler = handler;
    }


    @Override
    public void execute(TextChannel channel, String[] args, @NotNull GuildMessageReceivedEvent event) throws IOException {
        final String content = event.getMessage().getContentDisplay();
        final Matcher match = pattern.matcher(content);
        final String author = event.getAuthor().getAsTag();
        Path path = this.path.resolve(author + "-submissions.txt");
        channel.sendTyping().queue();
        MessageAction msg = channel.sendMessage("");
        if (match.find()) {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            String insult = match.group(0);
            msg.append("Thank you for your submission\n");
            if (checkIfInsultExists(insult)) {
                msg.append("It appears that this insult is either already existing or has been submitted before :)");
                return;
            }
            OutputStream out = Files.newOutputStream(path, StandardOpenOption.APPEND);
            out.write((insult + System.lineSeparator()).replaceAll("\"", "").getBytes(StandardCharsets.UTF_8));
            out.close();
            msg.append("Successfully submitted insult for review :)\nHopefully it will be added!").queue();
        } else {
            msg.append("Please make sure to type your insult in \"\" for me to recognize it :)").queue();
        }
        //actually sending the message now
        msg.queue();
    }

    private boolean checkIfInsultExists(String submission) {
        final String insult = submission.replaceAll("(\n|\r|\")", "");
        for (String handlerInsult : handler.getInsults()) {
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

}
