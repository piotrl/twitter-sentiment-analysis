package scrapper;

import java.time.LocalDate;

public class Tweet {
    private String partyName;
    private LocalDate day;
    private String tweet;

    public Tweet(String partyName, LocalDate day, String tweet) {
        this.partyName = partyName;
        this.day = day;
        this.tweet = tweet;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }
}
