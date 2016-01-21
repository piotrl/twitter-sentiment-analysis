package scrapper;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class TwitterApiUrl {

    private static final String ADVANCED_SEARCH_URL = "https://twitter.com/search?f=tweets&vertical=default" +
            "&q=lang%3Apl%20" +
            "%40{{username}}%20" +
            "since%3A{{from}}%20" +
            "until%3A{{to}}&" +
            "src=typd&lang=en";

    public String partyUrl(String politicUserName, LocalDate date) {
        String day = date.format(ISO_LOCAL_DATE);
        String nextDay = date.plusDays(1).format(ISO_LOCAL_DATE);
        return partyUrl(politicUserName, day, nextDay);
    }

    private String partyUrl(String politicUserName, String from, String to) {
        return ADVANCED_SEARCH_URL
                .replace("{{username}}", politicUserName)
                .replace("{{from}}", from)
                .replace("{{to}}", to);
    }
}
