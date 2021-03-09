package org.kurodev.discord.command.guild.standard.rockpaperscissors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.discord.command.argument.Argument;
import org.kurodev.discord.command.guild.CommandArgument;
import org.kurodev.discord.command.guild.GuildCommand;
import org.kurodev.discord.config.Setting;
import org.kurodev.discord.util.MarkDown;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author kuro
 **/
public class RockPaperScissorsCommand extends GuildCommand {
    private static final List<RPSCondition> CONDITION_LIST = new ArrayList<>();
    @CommandArgument(mandatory = true, meaning = "your choice for this round of the game")
    private static final String CHOICE = "choice"; //yes this is never used, still, please don't delete it.
    @CommandArgument(meaning = "Displays a list of all possible choices for the game")
    private static final String SHOW_OPTIONS = "--list";

    static {
        RPSCondition rock = addCondition("rock");
        RPSCondition paper = addCondition("paper");
        RPSCondition scissors = addCondition("scissors");
        rock.beats(scissors);
        paper.beats(rock);
        scissors.beats(paper);
    }

    private final Random random = new Random();

    public RockPaperScissorsCommand() {
        super("rps");
    }

    private static RPSCondition addCondition(String name) {
        RPSCondition condition = new RPSCondition(name);
        CONDITION_LIST.add(condition);
        return condition;
    }

    @Override
    public void prepare() throws Exception {
        logger.info("Checking files");
        final Path file = Paths.get(Main.SETTINGS.getSetting(Setting.RPS_Outcomes_File));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (!Files.exists(file)) {
            logger.info("creating {}", file.getFileName());
            String json = gson.toJson(CONDITION_LIST);
            Files.writeString(file, json);
            logger.info("creating {} - DONE", file.getFileName());
        } else {
            logger.info("Parsing RPSCondition data");
            Type listType = new TypeToken<ArrayList<RPSCondition>>() {
            }.getType();
            List<RPSCondition> list = gson.fromJson(new InputStreamReader(Files.newInputStream(file)), listType);
            CONDITION_LIST.clear();
            CONDITION_LIST.addAll(list);
            logger.info("Parsing RPSCondition data - DONE");
        }
    }

    @Override
    public String getDescription() {
        return "it's just a simple rock paper scissors game.";
    }

    @Override
    public void execute(TextChannel channel, Argument args, @NotNull GuildMessageReceivedEvent event) throws IOException {
        if (args.getOpt(SHOW_OPTIONS)) {
            channel.sendMessage(MarkDown.CODE_BLOCK.wrap(writeOptionString())).queue();
        } else if (args.getOtherArgs().isEmpty()) {
            channel.sendMessage("Argument required\n").append(writeOptionString()).queue();
        } else if (args.containsAny(getConditionNames())) {
            RPSCondition botChoice = getRandomRps();
            int playerChoiceIndex = 0;
            RPSCondition playerChoice = find(args.getOtherArgs().get(playerChoiceIndex));
            String outcome = "";
            switch (botChoice.check(playerChoice)) {
                case WIN:
                    outcome = "i win";
                    break;
                case LOSE:
                    outcome = "you win";
                    break;
                case DRAW:
                    outcome = "its a draw";
                    break;
            }
            channel.sendMessage("My choice: `" + botChoice.getName()).append("`\n")
                    .append("your choice: `").append(String.valueOf(playerChoice.getName()))
                    .append("`\nresult: `").append(outcome).append("`").queue();
        }
    }

    private String writeOptionString() {
        StringBuilder msg = new StringBuilder("Here are all choices:\n");
        List<String> conditionNames = getConditionNames();
        for (int i = 0; i < conditionNames.size(); i++) {
            String conditionName = conditionNames.get(i);
            msg.append(i + 1).append(")\t").append(conditionName).append("\n");
        }
        return msg.toString();
    }

    private RPSCondition getRandomRps() {
        return CONDITION_LIST.get(random.nextInt(CONDITION_LIST.size()));
    }

    private List<String> getConditionNames() {
        return CONDITION_LIST.stream().map(RPSCondition::getName).collect(Collectors.toList());
    }

    private RPSCondition find(String name) {
        return CONDITION_LIST.stream().filter(rpsCondition -> rpsCondition.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
