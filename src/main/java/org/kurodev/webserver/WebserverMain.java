package org.kurodev.webserver;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class WebserverMain implements Runnable {
    ConfigurableApplicationContext webApp;

    @Override
    public void run() {
        Class<?>[] classes = {RestController.class, WebConfig.class};
        webApp = SpringApplication.run(classes, new String[0]);
    }

    public void shutdown() {
        SpringApplication.exit(webApp);
    }
}
