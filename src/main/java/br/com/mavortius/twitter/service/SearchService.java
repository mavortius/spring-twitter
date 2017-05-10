package br.com.mavortius.twitter.service;

import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.Tweet;
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

    public List<Tweet> search(String searchType, List<String> keywords) {
        List<SearchParameters> searches = keywords.stream()
                                            .map(taste -> createSearchParameters(searchType, taste))
                                            .collect(Collectors.toList());
        List<Tweet> results = searches.stream()
                                .map(params -> twitter.searchOperations()
                                .search(params))
                                .flatMap(searchResults -> searchResults.getTweets().stream())
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
