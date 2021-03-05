package org.kurodev.command.guild.standard.rockpaperscissors;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;
import org.kurodev.command.guild.GuildCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author kuro
 **/
public class RockPaperScissorsCommand extends GuildCommand {
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
    public String getDescription() {
        return "it's just a simple rock paper scissors game. argument: -list to see all possible choices";
    }

    @Override
    public void execute(TextChannel channel, String[] args, @NotNull GuildMessageReceivedEvent event) throws IOException {
        if (argsContain(args, "-list")) {
            MessageAction action = channel.sendMessage("here are all choices:\n```\n");
            for (String conditionName : getConditionNames()) {
                action.append(conditionName).append("\n");
            }
            action.append("```").queue();
            return;
        }
        if (argsContain(args, getConditionNames())) {
            RPSCondition botChoice = getRandomRps();
            int argIndex = 0;
            RPSCondition playerChoice = find(args[argIndex]);
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
            channel.sendMessage("My choice: " + botChoice.getName()).append("\n")
                    .append("your choice: ").append(String.valueOf(playerChoice.getName()))
                    .append("\nresult: ").append(outcome).queue();
        }
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
