//package org.kurodev.command.privateMsg.console;
//
//import net.dv8tion.jda.api.requests.restaction.MessageAction;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author kuro
// **/
//@SuppressWarnings("ResultOfMethodCallIgnored")
//@Deprecated
//public class TimedConsole implements Runnable {
//    private static final int TIMEOUT_TIME = 10;
//    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
//    private final ProcessBuilder builder;
//    private ScheduledExecutorService executor;
//    private Process current;
//    private MyStreamReader reader;
//
//    public TimedConsole(ProcessBuilder builder) {
//        this.builder = builder;
//        executor = Executors.newSingleThreadScheduledExecutor();
//    }
//
//    public void execute(String command, MessageAction msg) throws IOException {
//        if (current == null) {
//            System.err.println("Starting new process");
//            createNewProcess();
//        }
//        reader.setMsg(msg);
//        Executors.newSingleThreadExecutor().execute(reader);
//        current.getOutputStream().write(command.getBytes(StandardCharsets.UTF_8));
//        current.getOutputStream().flush();
//        resetTimer();
//    }
//
//    private void resetTimer() {
//        executor.shutdownNow();
//        executor = Executors.newSingleThreadScheduledExecutor();
//        executor.schedule(this, TIMEOUT_TIME, TIME_UNIT);
//    }
//
//    private void createNewProcess() throws IOException {
//        current = builder.start();
//        reader = new MyStreamReader(current.getInputStream());
//        resetTimer();
//    }
//
//    @Override
//    public void run() {
//        //just resetting the current process.
//        current = null;
//        System.err.println("Terminating process");
//
//    }
//}
//
