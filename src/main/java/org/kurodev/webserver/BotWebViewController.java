package org.kurodev.webserver;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/bot")
public class BotWebViewController {
    @GetMapping("/")
    public RedirectView home() {
        RedirectView view = new RedirectView("/bot/index.html");
        return view;
    }
}
