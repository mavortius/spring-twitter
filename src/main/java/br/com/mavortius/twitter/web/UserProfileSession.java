package br.com.mavortius.twitter.web;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserProfileSession implements Serializable {
    private String twitterHandle;
    private String email;
    private LocalDate birthDate;
    private List<String> tastes = new ArrayList<>();
    private URL picturePath;

    public void save(ProfileForm form) {
        twitterHandle = form.getTwitterHandle();
        email = form.getEmail();
        birthDate = form.getBirthDate();
        tastes = form.getTastes();
    }

    public ProfileForm toForm() {
        ProfileForm form = new ProfileForm();

        form.setTwitterHandle(twitterHandle);
        form.setEmail(email);
        form.setBirthDate(birthDate);
        form.setTastes(tastes);

        return form;
    }

    public void setPicturePath(Resource picturePath) throws IOException {
        this.picturePath = picturePath.getURL();
    }

    public Resource getPicturePath() {
        return picturePath == null ? null : new UrlResource(picturePath);
    }
}
