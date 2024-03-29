package org.kurodev.discord.message.command.generic.rps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import org.kurodev.Main;
import org.kurodev.discord.message.command.AutoRegister;
import org.kurodev.discord.message.command.generic.GenericCommand;
import org.kurodev.config.Setting;
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
@AutoRegister
public class RockPaperScissorsCommand extends GenericCommand {
    private static final List<RPSCondition> CONDITION_LIST = new ArrayList<>();

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
    public void prepare(Options args) throws Exception {
        args.addOption("l", "list", false, "List all possible choices");
        Option choice = new Option("c", "choice", true, "The choice for this game");
        args.addOption(choice);
        logger.debug("Checking files");
        final Path file = Paths.get(Main.SETTINGS.getSetting(Setting.RPS_Outcomes_File));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (!Files.exists(file)) {
            logger.debug("creating {}", file.getFileName());
            String json = gson.toJson(CONDITION_LIST);
            Files.writeString(file, json);
        } else {
            logger.debug("Parsing RPSCondition data");
            Type listType = new TypeToken<ArrayList<RPSCondition>>() {
            }.getType();
            List<RPSCondition> list = gson.fromJson(new InputStreamReader(Files.newInputStream(file)), listType);
            CONDITION_LIST.clear();
            CONDITION_LIST.addAll(list);
        }
    }

    @Override
    public void execute(MessageChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) throws IOException {
        String value = args.getOptionValue("c");
        if (args.hasOption("l")) {
            channel.sendMessage(MarkDown.CODE_BLOCK.wrap(writeOptionString())).queue();
        } else if (value != null) {
            RPSCondition botChoice = getRandomRps();
            RPSCondition playerChoice = find(value);
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
        } else {
            channel.sendMessage("Unknown choice, here are all available choices:\n")
                    .append(MarkDown.CODE_BLOCK.wrap(writeOptionString())).queue();
        }
    }

    @Override
    public String getDescription() {
        return "it's just a simple rock paper scissors game.";
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
