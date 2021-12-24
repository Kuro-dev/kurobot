package org.kurodev.discord.util.py;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.message.command.generic.admin.AdminCommand;
import org.kurodev.discord.message.command.generic.console.MyStreamReader;
import org.kurodev.script.ScriptValidator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * TODO find out if a python runtime exists before processing any of this code
 * I suggest using the File.listFiles(FileFilter filter) method on each directory in the path.
 * This will make searching each directory simpler.
 * must be done in message event handler
 */
public class PythonScriptCommand extends AdminCommand {
    private final Path dir;
    private String description = null;
    private Path mainFile;

    public PythonScriptCommand(String command, Path dir) {
        super(command);
        this.dir = dir;
    }

    @Override
    protected void prepare(Options args) throws Exception {
        var validator = ScriptValidator.validate(dir);
        if (validator.hasErrors()) {
            setFailed(validator.getErrors());
            return;
        }
        var desc = Path.of(dir.toString(), "/description.txt");
        description = Files.readString(desc, StandardCharsets.UTF_8);
        mainFile = Path.of(dir.toString(), "/main.py");
    }

    @Override
    public void execute(MessageChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) throws IOException {
        StringBuilder response = new StringBuilder();
        //will fill the response with the data from the python code print() statements
        MyStreamReader reader = new MyStreamReader(null, response);

    }

    @Override
    public String getDescription() {
        return description;
    }
}
