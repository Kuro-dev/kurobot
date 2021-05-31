package org.kurodev.discord.message.command.generic.admin.force;

import net.dv8tion.jda.api.entities.MessageChannel;
import org.apache.commons.cli.Options;
import org.kurodev.discord.message.command.generic.admin.AdminCommand;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author kuro
 **/
public abstract class ForceAddFileCommand extends AdminCommand {
    protected final Path path;

    public ForceAddFileCommand(String command, Path path) {
        super("Force-add-" + command);
        this.path = path;
    }


    @Override
    public void prepare(Options args) throws Exception {
        if (!Files.exists(path)) {
            if (path.getFileName().toString().matches(".+\\..+")) {
                logger.info("Creating File: " + path);
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            } else {
                Files.createDirectories(path);
            }
        }
    }

    protected void onSuccess(MessageChannel channel) {
        channel.sendMessage("Successfully added file to database").queue();
    }
}
