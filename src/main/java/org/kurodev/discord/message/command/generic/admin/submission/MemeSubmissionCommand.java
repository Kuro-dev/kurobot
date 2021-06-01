package org.kurodev.discord.message.command.generic.admin.submission;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.config.Setting;
import org.kurodev.discord.util.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author kuro
 **/
public class MemeSubmissionCommand extends SubmissionCommand {

    public MemeSubmissionCommand() {
        this(Paths.get(Main.SETTINGS.getSetting(Setting.MEME_SUBMISSIONS)));
    }

    /**
     * @param dest Destination path for every submission of this type
     */
    public MemeSubmissionCommand(Path dest) {
        super("Meme", dest);
    }

    protected MemeSubmissionCommand(String command, Path dest) {
        super(command, dest);
    }


    @Override
    public void execute(MessageChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) throws IOException {
        if (event.getMessage().getAttachments().isEmpty()) {
            channel.sendMessage("Nothing was attached, I don't know what to submit :(").queue();
        } else {
            final String acceptedExtensions = "(jpg|png|gif|mp4)";
            for (Message.Attachment attachment : event.getMessage().getAttachments()) {
                if (attachment.getFileName().matches("(.+\\." + acceptedExtensions + ")")) {
                    Path destPath = Util.generateFileName(attachment.getFileName(), path);
                    File dest = destPath.toFile();
                    attachment.downloadToFile(dest).whenCompleteAsync((file, throwable) -> {
                        if (throwable != null) {
                            channel.sendMessage("Something went wrong when submitting: " + file)
                                    .append("\n").append(throwable.getMessage()).queue();
                        }
                    });
                } else {
                    channel.sendMessage("Unsupported file extension, only accepted are: " + acceptedExtensions)
                            .append("\nYour file: ").append(attachment.getFileName()).queue();
                }
                onSuccess(channel);
            }
        }
    }

    protected void onSuccess(MessageChannel channel) {
        channel.sendMessage("Thank you for your submission!\n")
                .append("Attachments have been submitted and will be reviewed :)\n")
                .append("If the reviewers think the submission is a good meme, it may be added to the database").queue();
    }

    @Override
    public String getDescription() {
        return "used to submit a meme, usage: attach an image to the command-message";
    }

}
