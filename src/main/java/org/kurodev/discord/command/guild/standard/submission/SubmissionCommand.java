package org.kurodev.discord.command.guild.standard.submission;

import org.kurodev.discord.command.guild.GuildCommand;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author kuro
 **/
public abstract class SubmissionCommand extends GuildCommand {
    protected final Path path;


    protected SubmissionCommand(String command, Path path) {
        super("Submit" + command);
        this.path = path;
    }

    @Override
    public void prepare() throws Exception {
        logger.info("Loading files");
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
}
