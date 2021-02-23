package org.kurodev.command.guild.force;

import net.dv8tion.jda.api.entities.TextChannel;
import org.kurodev.command.guild.admin.AdminCommand;

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
    public void prepare() throws Exception {
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

    protected void onSuccess(TextChannel channel) {
        channel.sendMessage("Successfully added file to database").queue();
    }
}
