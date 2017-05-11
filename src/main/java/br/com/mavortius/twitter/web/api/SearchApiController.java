package br.com.mavortius.twitter.web.api;

import br.com.mavortius.twitter.service.SearchService;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchApiController {

    private SearchService service;

    public SearchApiController(SearchService service) {
        this.service = service;
    }

    @RequestMapping(value = "/{searchType}", method = RequestMethod.GET)
    public List<Tweet> search(@PathVariable String searchType, @MatrixVariable List<String> keywords) {
        return service.search(searchType, keywords);
    }
}
