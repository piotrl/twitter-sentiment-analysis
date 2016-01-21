package scrapper;

import summary.DaySummary;
import words.Emotion;
import words.WordSemantic;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;
import static words.Emotion.*;

public class TweetScoreService {

    private List<WordSemantic> database;

    public TweetScoreService(List<WordSemantic> database) {
        this.database = database;
    }

    public DaySummary calcDaySummary(LocalDate day, List<TweetScore> tweetsScores) {
        List<TweetScore> dayTweets = tweetsScores.stream()
                .filter(tweetScore -> day.equals(tweetScore.getTweet().getDay()))
                .collect(toList());

        DaySummary daySummary = calcDaySummary(dayTweets);
        daySummary.setDay(day);
        return daySummary;
    }

    private DaySummary calcDaySummary(List<TweetScore> classifiedTweets) {
        AtomicInteger anger = new AtomicInteger();
        AtomicInteger happy = new AtomicInteger();
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
                    if (emotion.equals(NEUTRAL) || emotion.equals(UNCLASSIFIED)) {
                        neutral.incrementAndGet();
                    }
                });

        DaySummary daySummary = new DaySummary();
        daySummary.setANGER(anger.get());
        daySummary.setHAPPINESS(happy.get());
        daySummary.setSADNESS(sadness.get());
        daySummary.setFEAR(fear.get());
        daySummary.setDISGUST(disgust.get());
        daySummary.setUNCLASSIFIED(neutral.get());

        return daySummary;
    }

    public TweetScore calcTweet(Tweet tweet) {
        String content = tweet.getTweet();
        List<String> wordList = Arrays.asList(content.split(" "));
        TweetScore score = wordList.stream()
                .map(this::classifiedSemantic)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(new TweetScore(), (old, curr) -> {
                    old.setAnger(old.getAnger() + curr.getAnger());
                    old.setSadness(old.getSadness() + curr.getSadness());
                    old.setFear(old.getFear() + curr.getFear());
                    old.setDisgust(old.getDisgust() + curr.getDisgust());
                    old.setHappiness(old.getHappiness() + curr.getHappiness());
                    return old;
                }, (not, used) -> new TweetScore());

        score.setEmotion(calcEmotion(score));
        score.setTweet(tweet);
        return score;
    }

    private Emotion calcEmotion(TweetScore score) {
        Emotion emotion = UNCLASSIFIED;
        double max = 0.0;
        if (score.getAnger() > max) {
            emotion = ANGER;
            max = score.getAnger();
        }
        if (score.getDisgust() > max) {
            emotion = DISGUST;
            max = score.getDisgust();
        }
        if (score.getFear() > max) {
            emotion = FEAR;
            max = score.getFear();
        }
        if (score.getSadness() > max) {
            emotion = SADNESS;
            max = score.getSadness();
        }
        if (score.getHappiness() > max) {
            emotion = HAPPINESS;
        }

        return emotion;
    }

    private Optional<WordSemantic> classifiedSemantic(String word) {
        return database.stream()
                .filter(wordSemantic ->
                        StringSimilarity.similarity(wordSemantic.getName(), word) > 0.75
                )
                .findFirst();
    }
}
