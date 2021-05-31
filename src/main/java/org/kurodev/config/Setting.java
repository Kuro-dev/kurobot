package org.kurodev.config;

/**
 * @author kuro
 **/
public enum Setting {
    ALLOW_ADMIN_CONTACT("allow_admin_contact", "true"),
    DELETE_COMMAND_MESSAGE("delete_command_message", "false"),
    /**
     * Specifies whether or not bot messages should be deletable using the trash can reaction or not.
     */
    INCLUDE_DELETE_OPTION("include_delete_option", "false"),
    INSULT_FILE("insult_file", "./insults.txt"),
    INSULT_SUBMISSIONS("submission_insults", "./insultSubmissions"),
    MEME_FOLDER("meme_folder", "./memes"),
    MEME_SUBMISSIONS("submission_memes", "./memeSubmissions"),
    MEME_VOTE_FILE("meme_votes_file", "./memeVotes.json"),
    RPS_Outcomes_File("rps_outcomes", "./RPSOutcomes.json"),
    BOT_NAME("botName","Kuro-Bot"),
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
