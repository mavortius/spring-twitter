package br.com.mavortius.twitter.web.controller;

import br.com.mavortius.twitter.configuration.PicturesUploadProperties;
import br.com.mavortius.twitter.web.UserProfileSession;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.util.Locale;

@Controller
@SessionAttributes("picturePath")
public class PictureUploadController {
    private final Resource picturesDir;
    private final Resource anonymousPicture;
    private final MessageSource messageSource;
    private UserProfileSession userProfileSession;

    public PictureUploadController(PicturesUploadProperties properties, MessageSource messageSource, UserProfileSession userProfileSession) {
        this.picturesDir = properties.getUploadPath();
        this.anonymousPicture = properties.getAnonymousPicture();
        this.messageSource = messageSource;
        this.userProfileSession = userProfileSession;
    }

    @RequestMapping(value = "/profile", params = {"upload"}, method = RequestMethod.POST)
    public String onUpload(@RequestParam MultipartFile file, RedirectAttributes attributes) throws IOException {
        if(file.isEmpty() || !isImage(file)) {
            attributes.addFlashAttribute("error", "Incorrect file. Please upload a picture.");

            return "redirect:/profile";
        }

        Resource picturePath = copyFileToPictures(file);

        userProfileSession.setPicturePath(picturePath);

        return "redirect:/profile";
    }

    @RequestMapping(value = "/uploadedPicture")
    public void getUploadedPicture(HttpServletResponse response) throws IOException {
        Resource picturePath = userProfileSession.getPicturePath();

        if(picturePath == null) {
            picturePath = anonymousPicture;
        }
        response.setHeader("Content-Type", URLConnection.guessContentTypeFromName(picturePath.getFilename()));
        IOUtils.copy(picturePath.getInputStream(), response.getOutputStream());
    }

    @RequestMapping("uploadError")
    public ModelAndView onUploadError(Locale locale) {
        ModelAndView modelAndView = new ModelAndView("profile");

        modelAndView.addObject("error", messageSource.getMessage("upload.file-too-big", null, locale));
        modelAndView.addObject("profileForm", userProfileSession.toForm());

        return modelAndView;
    }

    @ModelAttribute("picturePath")
    public Resource picturePath() {
        return anonymousPicture;
    }

    @ExceptionHandler(IOException.class)
    public ModelAndView handleIOException(Locale locale) {
        ModelAndView modelAndView = new ModelAndView("profile");

        modelAndView.addObject("error", messageSource.getMessage("upload.io-exception", null, locale));
        modelAndView.addObject("profileForm", userProfileSession.toForm());

        return modelAndView;
    }

    private Resource copyFileToPictures(MultipartFile file) throws IOException {
        String fileExtension = getFileExtension(file.getOriginalFilename());
        File tempFile = File.createTempFile("pic", fileExtension, picturesDir.getFile());

        try (
                InputStream in = file.getInputStream();
                OutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return new FileSystemResource(tempFile);
    }

    private boolean isImage(MultipartFile file) {
        return file.getContentType().startsWith("image");
    }

    private static String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf("."));
    }
}
