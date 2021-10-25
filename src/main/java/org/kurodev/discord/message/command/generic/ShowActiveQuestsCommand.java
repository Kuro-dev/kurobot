package org.kurodev.discord.message.command.generic;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import org.kurodev.discord.message.quest.Quest;
import org.kurodev.discord.message.quest.QuestHandler;
import org.kurodev.discord.message.quest.UserData;
import org.kurodev.discord.util.MarkDown;

import java.io.IOException;

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
        var questsMap = questHandler.getQuests();
        final UserData invoker = UserData.of(event);
        final StringBuilder response = new StringBuilder();
        questsMap.forEach((userData, quests) -> {

        });
        if (args.hasOption("a")) {
            questsMap.forEach((userData, value) -> {
                for (Quest quest : value) {
                    if (!quest.isExpired())
                        appendQuestString(response, quest, userData);
                }
            });
        } else if (args.hasOption("c")) {
            questsMap.forEach((userData, value) -> {
                for (Quest quest : value) {
                    if (userData.channelMatches(invoker) && !quest.isExpired())
                        appendQuestString(response, quest, userData);
                }
            });
        } else {
            var q = questsMap.get(invoker);
            if (q != null) {
                for (Quest quest : q) {
                    appendQuestString(response, quest, invoker);
                }
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
