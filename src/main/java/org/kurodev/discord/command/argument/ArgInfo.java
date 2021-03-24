package org.kurodev.discord.command.argument;

/**
 * @author kuro
 **/
public class ArgInfo {
    private final String name;
    private final boolean mandatory;
    private final boolean requiresAdmin;
    private final String meaning;

    public ArgInfo(String name, boolean mandatory, boolean requiresAdmin, String meaning) {

        this.name = name;
        this.mandatory = mandatory;
        this.requiresAdmin = requiresAdmin;
        this.meaning = meaning;
    }

    public String getName() {
        return name;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public boolean RequiresAdmin() {
        return requiresAdmin;
    }

    public String getMeaning() {
        return meaning;
    }
}



