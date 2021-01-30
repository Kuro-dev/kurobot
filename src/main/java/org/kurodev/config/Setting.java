package org.kurodev.config;

/**
 * @author kuro
 **/
public enum Setting {
    MEME_FOLDER("meme.folder", ""),
    INSULT_FILE("insult.file", "./insults.txt"),
    INSULT_CHANCE("insult.chance", "1000"),
    TOKEN("token", ""),
    INSULT_SUBMISSIONS("submission.insults","./insultSubmissions"),
    MEME_SUBMISSIONS("submission.memes","./memeSubmissions"),
            ;

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
