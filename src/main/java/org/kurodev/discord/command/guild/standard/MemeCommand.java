package org.kurodev.discord.command.guild.standard;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.discord.command.guild.GuildCommand;
import org.kurodev.discord.config.Setting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author kuro
 **/
public class MemeCommand extends GuildCommand {
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
        if (event.getChannel().isNSFW()) {
            Path image = getRandomImage();
            if (image != null) {
                channel.sendMessage("There you go, enjoy :)").addFile(image.toFile()).queue();
                if (Main.SETTINGS.getSettingBool(Setting.DELETE_COMMAND_MESSAGE))
                    event.getMessage().delete().queue();
            } else {
                channel.sendMessage("No memes found :(").queue();
            }
        } else {
            channel.sendMessage("you may want to be in a NSFW channel for this...").queue();
        }
    }

    @Override
    public String getDescription() {
        return "sends a random meme from the meme folder";
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
