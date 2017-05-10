package br.com.mavortius.twitter.web.controller;

import br.com.mavortius.twitter.service.SearchService;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class SearchController {
    private SearchService service;

    public SearchController(SearchService service) {
        this.service = service;
    }

    @RequestMapping("/search/{searchType}")
    public ModelAndView search(@PathVariable String searchType, @MatrixVariable List<String> keywords) {
        List<Tweet> tweets = service.search(searchType, keywords);
        ModelAndView modelAndView = new ModelAndView("result");

        modelAndView.addObject("tweets", tweets);
        modelAndView.addObject("search", String.join(",", keywords));

        return modelAndView;
    }
}
