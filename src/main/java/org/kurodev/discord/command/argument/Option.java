package org.kurodev.discord.command.argument;

/**
 * @deprecated will soon be replaced}
 **/
@Deprecated(forRemoval = true, since = "1.7.0")
class Option {
    final String flag, opt;

    Option(String flag, String opt) {
        this.flag = flag;
        this.opt = opt;
    }
}
