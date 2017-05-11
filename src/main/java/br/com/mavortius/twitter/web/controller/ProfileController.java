package br.com.mavortius.twitter.web.controller;

import br.com.mavortius.twitter.domain.ProfileForm;
import br.com.mavortius.twitter.web.UserProfileSession;
import br.com.mavortius.twitter.web.formatter.USLocalDateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;

@Controller
public class ProfileController {

    private UserProfileSession userProfileSession;

    public ProfileController(UserProfileSession userProfileSession) {
        this.userProfileSession = userProfileSession;
    }

    @RequestMapping("/profile")
    public String display(ProfileForm profileForm) {
        return "profile";
    }

    @RequestMapping(value = "/profile", params = {"save"}, method = RequestMethod.POST)
    public String save(@Valid ProfileForm profileForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "profile";
        }

        userProfileSession.save(profileForm);

        return "redirect:/search/mixed;keywords=" + String.join(",", profileForm.getTastes());
    }

    @RequestMapping(value = "/profile", params = {"addTaste"})
    public String addTaste(ProfileForm profileForm) {
        profileForm.getTastes().add(null);

        return "profile";
    }

    @RequestMapping(value = "/profile", params = {"removeTaste"})
    public String removeTaste(ProfileForm profileForm, HttpServletRequest request) {
        Integer rowId = Integer.valueOf(request.getParameter("removeTaste"));

        profileForm.getTastes().remove(rowId.intValue());

        return "profile";
    }

    @ModelAttribute
    public ProfileForm getProfileForm() {
        return userProfileSession.toForm();
    }

    @ModelAttribute("dateFormat")
    public String localeFormat(Locale locale) {
        return USLocalDateFormatter.getPattern(locale);
    }
}
