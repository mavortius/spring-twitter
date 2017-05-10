package br.com.mavortius.twitter.web.controller;

import br.com.mavortius.twitter.web.UserProfileSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController {

    private UserProfileSession userProfileSession;

    public HomeController(UserProfileSession userProfileSession) {
        this.userProfileSession = userProfileSession;
    }

    @RequestMapping("/")
    public String home() {
        List<String> tastes = userProfileSession.toForm().getTastes();

        if(tastes.isEmpty()) {
            return "redirect:/profile";
        }

        return "redirect:/search/mixed;keywords=" + String.join(",", tastes);
    }
}
