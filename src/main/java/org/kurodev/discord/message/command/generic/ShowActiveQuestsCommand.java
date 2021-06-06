package org.kurodev.discord.message.command.generic;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.message.command.generic.GenericCommand;
import org.kurodev.discord.message.quest.UserData;
import org.kurodev.discord.message.quest.Quest;
import org.kurodev.discord.message.quest.QuestHandler;
import org.kurodev.discord.util.MarkDown;

import java.io.IOException;
import java.util.Map;

public class ShowActiveQuestsCommand extends GenericCommand {
    private final QuestHandler questHandler;


    public ShowActiveQuestsCommand(QuestHandler questHandler) {
        super("Quests");
        this.questHandler = questHandler;
    }

    @Override
    protected void prepare(Options args) throws Exception {
        args.addOption("a", "all", false, "Shows every currently active quest");
        args.addOption("c", "channel", false, "Shows every active quest in this channel");
    }

    @Override
    public void execute(MessageChannel channel, CommandLine args, @NotNull MessageReceivedEvent event) throws IOException {
        final Map<UserData, Quest> quests = questHandler.getQuests();
        final UserData invoker = UserData.of(event);
        final StringBuilder response = new StringBuilder();
        if (args.hasOption("a")) {
            quests.entrySet().stream().filter(e -> !e.getValue().isExpired())
                    .forEach(entry -> {
                        UserData player = entry.getKey();
                        appendQuestString(response, entry.getValue(), player);
                    });
        } else if (args.hasOption("c")) {
            quests.entrySet().stream().filter(e -> !e.getValue().isExpired() && e.getKey().channelMatches(invoker))
                    .forEach(entry -> {
                        UserData player = entry.getKey();
                        appendQuestString(response, entry.getValue(), player);
                    });
        } else {
            Quest q = quests.get(invoker);
            if (q != null) {
                appendQuestString(response, q, invoker);
            }
        }

        if (response.toString().isBlank()) {
            response.append("Currently there is no active quest");
        }
        channel.sendMessage(MarkDown.CODE_BLOCK.wrap(response.toString())).queue();
    }

    @Override
    public boolean needsAdmin() {
        return true;
    }

    @Override
    public boolean isListed() {
        return false;
    }

    @Override
    public String getDescription() {
        return "its a simple debug command to show all actively running quests";
    }

    private void appendQuestString(StringBuilder response, Quest quest, UserData data) {
        response.append(data.getUser().getAsTag()).append(": ").append(quest.getTitle()).append("\n");
    }
}
