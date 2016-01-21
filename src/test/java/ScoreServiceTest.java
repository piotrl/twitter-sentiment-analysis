import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import scrapper.Tweet;
import scrapper.TweetScore;
import scrapper.TweetScoreService;
import summary.DaySummary;
import words.WordDatabase;
import words.WordSemantic;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;
import static words.Emotion.*;

public class ScoreServiceTest {

    private List<WordSemantic> semantics;
    private TweetScoreService service;

    @Before
    public void init() {
        WordDatabase wordDatabase = new WordDatabase();
        this.semantics = wordDatabase.importDb();
        service = new TweetScoreService(this.semantics);
    }

    @Test
    public void scoreTweet() {
        Tweet tweet = new Tweet(
                "pisorgpl",
                LocalDate.now(),
                "@mariebasz @PremierRP Woodstock tak dziś wyglądał w godzina W. I żaden gamoń z @pisorgpl tego nie zakłamie. pic.twitter.comOFfjxxcy2"
        );

        TweetScoreService service = new TweetScoreService(semantics);
        TweetScore tweetScore = service.calcTweet(tweet);
        Assert.assertNotNull(tweetScore.getEmotion());
        Assert.assertNotSame(0, tweetScore.getHappiness());
    }

    @Test
    public void daySummaryNowoczesna() {
        String partyName = "Kukiz15";
        List<Tweet> pisTweets = FileUtil.loadFromCsv(partyName);
        List<DaySummary> daySummaryList = calcDaySummaries(pisTweets);
        FileUtil.saveSummaryToCsv(daySummaryList, partyName);
    }

    @Test
    public void daySummaryPo() {
        String partyName = "Platforma_org";
        List<Tweet> pisTweets = FileUtil.loadFromCsv(partyName);
        List<DaySummary> daySummaryList = calcDaySummaries(pisTweets);
        FileUtil.saveSummaryToCsv(daySummaryList, partyName);
    }

    private List<DaySummary> calcDaySummaries(List<Tweet> pisTweets) {
        List<TweetScore> scores = getTweetScores(pisTweets);
        List<LocalDate> days = scores.stream()
                .map(TweetScore::getTweet)
                .map(Tweet::getDay)
                .distinct()
                .collect(toList());

        return days.stream()
                .map(day -> service.calcDaySummary(day, scores))
                .collect(toList());
    }

    @Test
    public void scoreTweetsProcedure() {
        String partyName = "partia_korwin";
        List<Tweet> pisTweets = FileUtil.loadFromCsv(partyName);

        List<TweetScore> scores = getTweetScores(pisTweets);
        List<TweetScore> classifiedTweets = scores.stream()
                .filter(score -> score.getEmotion() != UNCLASSIFIED)
                .collect(toList());

        System.out.println("Summary");
        System.out.println("Unclassified tweets: " + (scores.size() - classifiedTweets.size()));
        printSummary(classifiedTweets);
    }

    private List<TweetScore> getTweetScores(List<Tweet> pisTweets) {
        Assert.assertFalse(pisTweets.isEmpty());

        TweetScoreService service = new TweetScoreService(semantics);
        return pisTweets.stream()
                .map(service::calcTweet)
                .collect(toList());
    }

    private void printSummary(List<TweetScore> classifiedTweets) {
        double size = classifiedTweets.size();
        AtomicInteger happy = new AtomicInteger();
        AtomicInteger anger = new AtomicInteger();
        AtomicInteger sadness = new AtomicInteger();
        AtomicInteger fear = new AtomicInteger();
        AtomicInteger disgust = new AtomicInteger();
        AtomicInteger neutral = new AtomicInteger();

        classifiedTweets.stream()
                .map(TweetScore::getEmotion)
                .forEach(emotion -> {
                    if (emotion.equals(ANGER)) {
                        anger.incrementAndGet();
                    }
                    if (emotion.equals(SADNESS)) {
                        sadness.incrementAndGet();
                    }
                    if (emotion.equals(HAPPINESS)) {
                        happy.incrementAndGet();
                    }
                    if (emotion.equals(FEAR)) {
                        fear.incrementAndGet();
                    }
                    if (emotion.equals(DISGUST)) {
                        disgust.incrementAndGet();
                    }
                    if (emotion.equals(UNCLASSIFIED)) {
                        neutral.incrementAndGet();
                    }
                });

        System.out.println("HAPPINESS: " + (happy.get() / size) + "%");
        System.out.println("ANGER: " + (anger.get() / size) + "%");
        System.out.println("SADNESS: " + (sadness.get() / size) + "%");
        System.out.println("FEAR: " + (fear.get() / size) + "%");
        System.out.println("DISGUST: " + (disgust.get() / size) + "%");
        System.out.println("NEUTRAL: " + (neutral.get() / size) + "%");
    }
}
