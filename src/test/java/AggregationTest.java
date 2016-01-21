import org.fluentlenium.adapter.FluentTest;
import org.fluentlenium.core.Fluent;
import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.junit.Test;
import scrapper.Tweet;
import scrapper.TwitterApiUrl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class AggregationTest extends FluentTest {
    private final static int TWEETS_PER_DAY = 30;

    @Test
    public void pis() {
        String partyName = "pisorgpl";
        buildTweetDatabase(partyName);
    }

    @Test
    public void po() {
        String partyName = "Platforma_org";
        buildTweetDatabase(partyName);
    }

    @Test
    public void korwin() {
        String partyName = "partia_korwin";
        buildTweetDatabase(partyName);
    }
    @Test
    public void nowoczesna() {
        String partyName = "_Nowoczesna";
        buildTweetDatabase(partyName);
    }

    @Test
    public void Kukiz15() {
        String partyName = "Kukiz15";
        buildTweetDatabase(partyName);
    }

    private void buildTweetDatabase(String partyName) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusMonths(6);
        List<Tweet> tweets = new ArrayList<>();
        for (LocalDate day = start; day.isBefore(end); day = day.plusDays(1)) {
            List<Tweet> textContents = tweetsFromOneDay(partyName, day);
            tweets.addAll(textContents);

            System.out.println("@" + partyName + " | " + day + " | " + textContents.size());
        }

        FileUtil.saveTweetsToCsv(tweets, partyName);
        System.out.println("Size: " + tweets.size());
    }

    private List<Tweet> tweetsFromOneDay(String party, LocalDate day) {
        TwitterApiUrl scrapper = new TwitterApiUrl();
        String url = scrapper.partyUrl(party, day);
        Fluent page = goTo(url);
        FluentList<FluentWebElement> $tweets = getTweets(page);

        return $tweets.getTextContents().stream()
                .map(tweetText -> new Tweet(party, day, tweetText))
                .collect(toList());
    }

    private FluentList<FluentWebElement> getTweets(Fluent page) {
        int tweetsAmount = 0;

        FluentList<FluentWebElement> $tweets = null;
        while(tweetsAmount < TWEETS_PER_DAY) {
            page.executeScript("window.scrollTo(0,document.body.scrollHeight)", "");
            $tweets = page.find(".original-tweet .TweetTextSize");

            try {
                FluentWebElement $backToTop = page.findFirst(".back-to-top");
                FluentWebElement $streamEnd = page.findFirst(".stream-end");
                if ($backToTop.isDisplayed() || $streamEnd.isDisplayed() || $tweets.isEmpty()) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            tweetsAmount = $tweets.size();
            page.await();
        }

        return $tweets;
    }
}
