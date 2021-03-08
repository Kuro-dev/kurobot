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
    QUOTE("> ", false),
    CODE("`"),
    CODE_BLOCK("```\n"),
    ;

    private final String markdown;
    private final boolean bothSides;

    MarkDown(String s) {
        this(s, true);
    }


    MarkDown(String s, boolean bothSides) {
        this.markdown = s;
        this.bothSides = bothSides;
    }

    public String wrap(String content) {
        boolean isTextBlock = content.contains("\n") || content.contains("\r");
        if (isTextBlock && this == QUOTE) {
            StringBuilder builder = new StringBuilder();
            for (String line : content.split("\n|\r")) {
                builder.append(markdown).append(line).append(bothSides ? markdown : "").append("\n");
            }
            return builder.toString();
        } else {
            return markdown + content + (bothSides ? markdown : "");
        }
    }

}
