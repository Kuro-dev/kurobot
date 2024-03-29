package org.kurodev.discord.message.command.generic.admin.force;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.config.Setting;
import org.kurodev.discord.message.command.AutoRegister;
import org.kurodev.discord.util.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author kuro
 **/
@AutoRegister
public class ForceAddMemeCommand extends ForceAddFileCommand {
    public ForceAddMemeCommand() {
        super("Meme", Paths.get(Main.SETTINGS.getSetting(Setting.MEME_FOLDER)));
    }

    @Override
    public String getDescription() {
        return "used to add a meme into the official database";
    }

    @Override
    public void execute(MessageChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) throws IOException {
        if (event.getMessage().getAttachments().isEmpty()) {
            channel.sendMessage("Nothing was attached, I don't know what to add").queue();
        } else {
            final String acceptedExtensions = "(jpg|png|gif|mp4)";
            for (Message.Attachment attachment : event.getMessage().getAttachments()) {
                if (attachment.getFileName().matches("(.+\\." + acceptedExtensions + ")")) {
                    Path destPath = Util.generateFileName(attachment.getFileName(), path);
                    File dest = destPath.toFile();
                    attachment.downloadToFile(dest).whenCompleteAsync((file, throwable) -> {
                        if (throwable != null) {
                            channel.sendMessage("Something went wrong when adding the file: " + file)
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
}
