package org.kurodev.discord.config;

/**
 * @author kuro
 **/
public enum Setting {
    ALLOW_ADMIN_CONTACT("allow.admin.contact", "true"),
    DELETE_COMMAND_MESSAGE("delete.command.message", "false"),
    /**
     * Specifies whether or not bot messages should be deletable using the trash can reaction or not.
     */
    INCLUDE_DELETE_OPTION("include.delete.option", "true"),
    INSULT_CHANCE("insult.chance", "1000"),
    INSULT_FILE("insult.file", "./insults.txt"),
    INSULT_SUBMISSIONS("submission.insults", "./insultSubmissions"),
    MEME_FOLDER("meme.folder", "./memes"),
    MEME_SUBMISSIONS("submission.memes", "./memeSubmissions"),
    MEME_VOTE_FILE("meme.votes.file", "./memeVotes.json"),
    RPS_Outcomes_File("rps.outcomes", "./RPSOutcomes.json"),
    TOKEN("token", "");

    private final String key;
    private final String defaultVal;

    Setting(String key, String defaultVal) {
        this.key = key;
        this.defaultVal = defaultVal;
    }

    public String getKey() {
        return key;
    }

    public String getDefaultVal() {
        return defaultVal;
    }
}
