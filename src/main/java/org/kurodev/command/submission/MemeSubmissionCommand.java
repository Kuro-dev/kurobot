package org.kurodev.command.submission;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author kuro
 **/
public class MemeSubmissionCommand extends SubmissionCommand {

    public MemeSubmissionCommand() {
        this(Paths.get("./memeSubmissions"));
    }

    /**
     * @param dest Destination path for every submission of this type
     */
    public MemeSubmissionCommand(Path dest) {
        super("Meme", dest, false);
    }


    @Override
    public void execute(TextChannel channel, String[] args, @NotNull GuildMessageReceivedEvent event) throws IOException {
        if (event.getMessage().getAttachments().isEmpty()) {
            channel.sendMessage("Nothing was attached, I don't know what to submit :(").queue();
        } else {
            final String acceptedExtensions = "(jpg|png|gif)";
            for (Message.Attachment attachment : event.getMessage().getAttachments()) {
                if (attachment.getFileName().matches("(.+\\." + acceptedExtensions + ")")) {
                    Path destPath = generateFileName(attachment.getFileName());
                    File dest = destPath.toFile();
                    attachment.downloadToFile(dest).whenCompleteAsync((file, throwable) -> {
                        if (throwable != null) {
                            channel.sendMessage("Something went wrong when submitting: " + file)
                                    .append("\n").append(throwable.getMessage()).queue();
                        }
                    });
                } else {
                    channel.sendMessage("Unsupported file extenion, only accepted are: " + acceptedExtensions)
                            .append("\nYour file: ").append(attachment.getFileName()).queue();
                }
            }
            channel.sendMessage("Thank you for your submission!\n")
                    .append("Attachments have been submitted and will be reviewed :)\n")
                    .append("If the reviewers think the submission is a good meme, it may be added to the database").queue();
        }
    }

    private Path generateFileName(String fileNameRaw) {
        var split = fileNameRaw.split("\\.");
        StringBuilder fileNameBuilder = new StringBuilder();
        String fileName, fileExtension = "." + split[split.length - 1];
        for (int i = 0; i < (split.length - 1); i++) {
            fileNameBuilder.append(split[i]);
        }
        fileName = fileNameBuilder.toString();
        Path file = path.resolve(fileName + fileExtension);

        for (int num = 1; Files.exists(file); num++) {
            String numString = String.format("(%d)", num);
            file = path.resolve(fileName + numString + fileExtension);
        }
        return file;
    }
}
