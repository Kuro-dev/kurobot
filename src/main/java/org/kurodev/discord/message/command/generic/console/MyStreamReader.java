package org.kurodev.discord.message.command.generic.console;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

/**
 * @author kuro
 **/
@SuppressWarnings("ResultOfMethodCallIgnored")
public class MyStreamReader implements Runnable, Consumer<String> {
    private final InputStream inputStream;
    private StringBuilder msg;

    public MyStreamReader(InputStream inputStream, StringBuilder msg) {
        this.inputStream = inputStream;
        this.msg = msg;
    }

    public void setMsg(@NotNull StringBuilder msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(this);
    }

    @Override
    public void accept(String s) {
        msg.append(s).append("\n");
    }
}
