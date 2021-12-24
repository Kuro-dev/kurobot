package org.kurodev.webserver;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import net.dv8tion.jda.api.Permission;
import org.kurodev.Main;
import org.kurodev.config.MySettings;
import org.kurodev.discord.util.information.CommandInformation;
import org.kurodev.discord.util.information.DiscordInfo;
import org.kurodev.discord.util.information.DiscordInfoCollector;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.EnumSet;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class DiscordInfoApi {

    @GetMapping(
            value = "/info",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ExternalDocumentation(description = "Returns all information on the bot")
    public ResponseEntity<DiscordInfo> getDiscordInfo() {
        DiscordInfo info = DiscordInfoCollector.getInstance().collect();
        return ResponseEntity.ok(info);
    }

    @GetMapping(
            value = "/permissions",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ExternalDocumentation(description = "Returns all required permissions the bot needs")
    public ResponseEntity<EnumSet<Permission>> getNeededPermissions() {
        var info = DiscordInfoCollector.getInstance().collect();
        EnumSet<Permission> perms = EnumSet.noneOf(Permission.class);
        for (CommandInformation commandInformation : info.getCommandInfo()) {
            System.out.println(commandInformation.getRequiredPerms());
            perms.addAll(commandInformation.getRequiredPerms());
        }
        return ResponseEntity.ok(perms);
    }

    @GetMapping(
            value = "/config",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ExternalDocumentation(description = "Returns the contents of the config file")
    public ResponseEntity<MySettings> getConfigInfo() {
        MySettings info = Main.SETTINGS;
        return ResponseEntity.ok(info);
    }

    @PutMapping(value = "/config", consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Accept=application/json")
    @ExternalDocumentation(description = """
            updates the contents of the config file to the given config file
            will possibly be updated later to allow for single value overrides
            ps currently not functioning
            """)
    public ResponseEntity<Void> setConfigInfo(@RequestBody MySettings config) {
        System.out.println("test: " + config);
        return ResponseEntity.ok(null);
    }
}
