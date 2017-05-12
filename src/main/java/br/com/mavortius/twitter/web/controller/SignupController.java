package br.com.mavortius.twitter.web.controller;

import br.com.mavortius.twitter.web.AuthenticatingSignInAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
public class SignupController {

    private final ProviderSignInUtils signInUtils;

    public SignupController(ConnectionFactoryLocator locator, UsersConnectionRepository repository) {
        this.signInUtils = new ProviderSignInUtils(locator, repository);
    }

    @RequestMapping(value = "/signup")
    public String signup(WebRequest request) {
        Connection<?> connection = signInUtils.getConnectionFromSession(request);

        if (connection != null) {
            AuthenticatingSignInAdapter.authenticate(connection);
            signInUtils.doPostSignUp(connection.getDisplayName(), request);
        }

        return "redirect:/profile";
    }
}
