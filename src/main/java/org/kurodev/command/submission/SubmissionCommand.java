package org.kurodev.command.submission;

import org.kurodev.command.standard.Command;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author kuro
 **/
public abstract class SubmissionCommand extends Command {
    protected final Path path;
    private final boolean isFile;

    /**
     * @param path Destination path for every submission of this type
     */
    public SubmissionCommand(String submissionType, Path path, boolean isFile) {
        super("submit" + submissionType);
        this.path = path;
        this.isFile = isFile;
    }

    @Override
    public void prepare() throws Exception {
        if (!Files.exists(path)) {
            if (isFile) {
                logger.info("Creating File: " + path.getFileName());
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            } else {
                Files.createDirectories(path);
            }
        }
    }
}
