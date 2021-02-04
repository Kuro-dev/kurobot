package org.kurodev.command.privateMsg.console;

import net.dv8tion.jda.api.requests.restaction.MessageAction;
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
    private MessageAction msg;

    public MyStreamReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setMsg(@NotNull MessageAction msg) {
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
