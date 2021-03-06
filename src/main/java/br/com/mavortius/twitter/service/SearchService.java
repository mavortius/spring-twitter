package br.com.mavortius.twitter.service;

import br.com.mavortius.twitter.domain.LightTweet;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private Twitter twitter;

    public SearchService(Twitter twitter) {
        this.twitter = twitter;
    }

    public List<LightTweet> search(String searchType, List<String> keywords) {
        List<SearchParameters> searches = keywords.stream()
                                            .map(taste -> createSearchParameters(searchType, taste))
                                            .collect(Collectors.toList());
        List<LightTweet> results = searches.stream()
                                .map(params -> twitter.searchOperations()
                                .search(params))
                                .flatMap(searchResults -> searchResults.getTweets().stream())
                                .map(LightTweet::ofTweet)
                                .collect(Collectors.toList());
        return results;
    }

    private SearchParameters createSearchParameters(String searchType, String taste) {
        SearchParameters.ResultType resultType = getResultType(searchType);
        SearchParameters parameters = new SearchParameters(taste);

        parameters.resultType(resultType);
        parameters.count(3);

        return parameters;
    }

    private SearchParameters.ResultType getResultType(String searchType) {
        for(SearchParameters.ResultType knownType : SearchParameters.ResultType.values()) {
            if(knownType.name().equalsIgnoreCase(searchType)) {
                return knownType;
            }
        }
        return SearchParameters.ResultType.RECENT;
    }
}
