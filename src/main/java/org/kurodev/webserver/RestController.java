package org.kurodev.webserver;

import com.google.gson.Gson;
import org.kurodev.Main;
import org.kurodev.config.MySettings;
import org.kurodev.discord.util.information.DiscordInfo;
import org.kurodev.discord.util.information.DiscordInfoCollector;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

@SpringBootApplication
@Controller
public class RestController {
    private final Gson gson = new Gson();

    @GetMapping("/")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index.html");
        return modelAndView;
    }

    @GetMapping(
            value = "/discordInfo",
            produces = MediaType.APPLICATION_JSON_VALUE,
            headers = "Access-Control-Allow-Origin: *")
    public ResponseEntity<String> getDiscordInfo() {
        DiscordInfo info = DiscordInfoCollector.getInstance().collect();
        return ResponseEntity.ok(gson.toJson(info));
    }

    @GetMapping(
            value = "/config",
            produces = MediaType.APPLICATION_JSON_VALUE,
            headers = "Access-Control-Allow-Origin: *")
    public ResponseEntity<String> getConfigInfo() {
        MySettings info = Main.SETTINGS;
        return ResponseEntity.ok(gson.toJson(info));
    }

    @PutMapping(value = "/config", consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json\nAccess-Control-Allow-Origin: *")
    public ResponseEntity<Void> setConfigInfo(@RequestBody MySettings config) {
        System.out.println("test: " + config);
        return ResponseEntity.ok(null);
    }
}
