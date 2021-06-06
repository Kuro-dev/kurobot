package discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.kurodev.Main;
import org.kurodev.discord.DiscordBot;
import org.kurodev.discord.UserIDs;
import org.kurodev.discord.util.MarkDown;

/**
 * @author kuro
 **/
public class IntegrationTest {
    private static JDA jda;
    private static User kuro;

    @BeforeClass
    public static void prepare() throws Exception {
        Main.main(new String[0]);
        jda = DiscordBot.getJda();
        kuro = UserIDs.KURO.getUser();
    }

    @AfterClass
    public static void cleanup() {
        jda.shutdown();
    }

    @Test
    @Ignore
    public void MarkDownTest() {
        PrivateChannel privateChannel = kuro.openPrivateChannel().complete();
        privateChannel.sendMessage(writeExample(MarkDown.BOLD)).complete();
        privateChannel.sendMessage(writeExample(MarkDown.ITALICS)).complete();
        privateChannel.sendMessage(writeExample(MarkDown.QUOTE)).complete();
        privateChannel.sendMessage(MarkDown.QUOTE.wrap("This is\nA multiline\n Quote")).complete();
        privateChannel.sendMessage(writeExample(MarkDown.UNDERLINE)).complete();
        privateChannel.sendMessage(writeExample(MarkDown.STRIKETHROUGH)).complete();
        privateChannel.sendMessage(writeExample(MarkDown.CODE)).complete();
        privateChannel.sendMessage(MarkDown.CODE_BLOCK.wrap("This is\nA code block")).complete();
        privateChannel.sendMessage("-----------DONE------------").complete();
    }

    private String writeExample(MarkDown mark) {
        return mark.wrap(mark.name());
    }

}
