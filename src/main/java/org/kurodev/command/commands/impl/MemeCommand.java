package org.kurodev.command.commands.impl;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.command.commands.Command;
import org.kurodev.conf.Setting;

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
    Path memeFolder = Paths.get(Main.getSettings().getSetting(Setting.MEME_FOLDER));
    private boolean loadSuccess;
    private long files;

    public MemeCommand() {
        super("meme");
        try {
            files = Files.list(memeFolder).filter(Files::isRegularFile).count();
            logger.info("Successfully loaded meme folder");
            loadSuccess = true;
        } catch (IOException e) {
            logger.error("Failed to load meme folder", e);
            loadSuccess = false;
        }
    }

    @Override
    public void execute(TextChannel channel, String[] args, @NotNull GuildMessageReceivedEvent event) throws IOException {
        if (loadSuccess){
            channel.sendMessage("There you go").complete();
            channel.sendFile(getRandomImage().toFile()).complete();
        }else {
            channel.sendMessage("Failed to load meme database, please try again once the bot has restarted or contact the developer").queue();
        }
    }

    private Path getRandomImage() throws IOException {
        return Files.list(memeFolder).filter(Files::isRegularFile).collect(Collectors.toList()).get(new Random().nextInt(Math.toIntExact(files)));
    }
}
