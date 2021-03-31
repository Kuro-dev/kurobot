package org.kurodev.discord.command.guild.standard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.discord.command.guild.CommandArgument;
import org.kurodev.discord.command.guild.GuildCommand;
import org.kurodev.discord.command.interfaces.Reactable;
import org.kurodev.discord.config.Setting;
import org.kurodev.discord.util.cache.Cache;
import org.kurodev.discord.vote.Score;
import org.kurodev.discord.vote.impl.ReactionVote;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author kuro
 **/
public class MemeCommand extends GuildCommand implements Reactable {
    private static final Pattern IS_NUMERIC_REG = Pattern.compile("\\d+");
    private final Path memeFolder = Paths.get(Main.SETTINGS.getSetting(Setting.MEME_FOLDER));
    private final Path memeVoteFile = Paths.get(Main.SETTINGS.getSetting(Setting.MEME_VOTE_FILE));
    private final Cache<List<Path>> fileCache = new Cache<>(1, TimeUnit.DAYS);
    private final Random random = new Random();
    private ReactionVote<String> vote = new ReactionVote<>();

    public MemeCommand() {
        super("Meme");
    }

    @Override
    public void prepare(Options args) throws Exception {
        args.addOption("count", "Gives the amount of memes present in the database");
        args.addOption("i", "index", true,
                "Gives an index to a specific file (must be numeric)");
        if (!Files.exists(memeFolder)) {
            logger.info("Creating Meme Folder");
            Files.createDirectories(memeFolder);
        }
        fileCache.setOnDirty(() -> {
            try {
                return Files.list(memeFolder).filter(Files::isRegularFile).collect(Collectors.toList());
            } catch (IOException e) {
                logger.error("Failed to fetch files", e);
                return Collections.emptyList();
            }
        });
        if (!Files.exists(memeVoteFile)) {
            logger.info("Creating Meme Vote File");
            Files.createFile(memeVoteFile);
        } else {
            Type type = new TypeToken<ReactionVote<String>>() {
            }.getType();
            ByteArrayInputStream bis = new ByteArrayInputStream(Files.readAllBytes(memeVoteFile));
            vote = new Gson().fromJson(new InputStreamReader(bis), type);
        }
    }

    @Override
    public void onShutdown() throws Exception {
        Files.writeString(memeVoteFile, new GsonBuilder().setPrettyPrinting().create().toJson(vote));
    }

    @Override
    public void execute(TextChannel channel, CommandLine args, @NotNull GuildMessageReceivedEvent event) throws IOException {
        if (args.hasOption("count")) {
            channel.sendMessage("index: ").append(String.valueOf(fileCache.getCachedItem().size() - 1)).queue();
            return;
        }
        if (event.getChannel().isNSFW()) {
            final String index = args.getOptionValue("i");
            final boolean indexIsValid = index != null && IS_NUMERIC_REG.matcher(index).matches();
            Path image = null;
            final List<Path> files = fileCache.getCachedItem();
            int imgNum;
            if (indexIsValid) {
                imgNum = Integer.parseInt(index);
                if (files.size() > imgNum) {
                    image = files.get(imgNum);
                } else {
                    channel.sendMessage(String.format("Index is out of bounds. max: %d", files.size() - 1)).queue();
                }
            } else {
                imgNum = getRandomImageIndex();
                if (imgNum != -1)
                    image = files.get(imgNum);
            }

            if (image != null) {
                channel.sendMessage(createImageDesc(image, imgNum)).addFile(image.toFile()).queue(vote::makeVoteable);
                if (Main.SETTINGS.getSettingBool(Setting.DELETE_COMMAND_MESSAGE))
                    event.getMessage().delete().queue();
            } else {
                channel.sendMessage("No memes found :(").queue();
            }
        } else {
            channel.sendMessage("you may want to be in a NSFW channel for this...").queue();
        }
    }

    private String createImageDesc(Path image, int index) {
        Score score = vote.get(image.getFileName().toString());
        return String.format("Here you go, enjoy!\n`Index: %d, upvotes: %d, down votes: %d, Score: %d`",
                index, score.getUpVotes(), score.getDownVotes(), score.getScore());
    }

    @Override
    public String getDescription() {
        return "sends a random meme from the meme folder";
    }

    private int getRandomImageIndex() {
        final long files = fileCache.getCachedItem().size();
        if (files > 0) {
            return random.nextInt(Math.toIntExact(files));
        } else
            return -1;
    }

    @Override
    public void onReact(Message message, GuildMessageReactionAddEvent event) {
        for (Message.Attachment attachment : message.getAttachments()) {
            if (attachment.isImage()) {
                String reaction = event.getReaction().getReactionEmote().getAsReactionCode();
                vote.handleReaction(reaction, attachment.getFileName(), true);
            }
        }
    }

    @Override
    public void onReact(Message message, GuildMessageReactionRemoveEvent event) {
        for (Message.Attachment attachment : message.getAttachments()) {
            if (attachment.isImage()) {
                String reaction = event.getReaction().getReactionEmote().getAsReactionCode();
                vote.handleReaction(reaction, attachment.getFileName(), false);
            }
        }
    }
}
