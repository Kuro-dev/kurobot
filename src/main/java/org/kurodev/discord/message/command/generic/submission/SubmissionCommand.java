package org.kurodev.discord.message.command.generic.submission;

import org.apache.commons.cli.Options;
import org.kurodev.discord.message.command.generic.GenericCommand;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author kuro
 **/
public abstract class SubmissionCommand extends GenericCommand {
    protected final Path path;


    protected SubmissionCommand(String command, Path path) {
        super("Submit" + command);
        this.path = path;
    }

    @Override
    public void prepare(Options args) throws Exception {
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
