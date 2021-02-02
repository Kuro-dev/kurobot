package org.kurodev.command.standard;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.command.Command;
import org.kurodev.config.Setting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author kuro
 **/
public class MemeCommand extends Command {
    Path memeFolder = Paths.get(Main.SETTINGS.getSetting(Setting.MEME_FOLDER));

    public MemeCommand() {
        super("Meme");
    }

    @Override
    public void prepare() throws Exception {
        if (!Files.exists(memeFolder)) {
            logger.info("Creating Meme Folder");
            Files.createDirectories(memeFolder);
        }
    }

    @Override
    public void execute(TextChannel channel, String[] args, @NotNull GuildMessageReceivedEvent event) throws IOException {
        Path image = getRandomImage();
        if (image != null) {
            channel.sendMessage("There you go, enjoy the meme ").append(event.getAuthor().getAsMention()).append(" :)\n").addFile(image.toFile()).queue();
            event.getMessage().delete().queue();
        } else
            channel.sendMessage("No memes found").queue();
    }

    private Path getRandomImage() throws IOException {
        final long files = Files.list(memeFolder).filter(Files::isRegularFile).count();
        if (files > 0)
            return Files.list(memeFolder).filter(Files::isRegularFile).collect(Collectors.toList()).get(new Random().nextInt(Math.toIntExact(files)));
        else {
            return null;
        }
    }
}