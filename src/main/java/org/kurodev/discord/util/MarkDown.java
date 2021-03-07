package org.kurodev.discord.util;

/**
 * @author kuro
 **/
//TODO finish this up
public enum MarkDown {
    ITALICS("*"),
    BOLD("**"),
    UNDERLINE("__"),
    STRIKETHROUGH("~~"),
    QUOTE("> ", false, ">>>"),
    CODE_BLOCK("`", "```\n");

    private final String markdown;
    private final boolean bothSides;
    private final boolean supportBlock;
    private final String blockOption;

    MarkDown(String s) {
        this(s, true);
    }

    MarkDown(String s, String blockOption) {
        this(s, true, blockOption);
    }

    MarkDown(String s, boolean bothSides, String blockOption) {
        this.markdown = s;
        this.bothSides = bothSides;
        supportBlock = blockOption == null || blockOption.isBlank();
        this.blockOption = blockOption;
    }

    MarkDown(String s, boolean bothSides) {
        this(s, bothSides, null);
    }

    public String wrap(String content, boolean isTextBlock) {
        if (isTextBlock && supportBlock) {
            return blockOption + content + (bothSides ? blockOption : "");
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
