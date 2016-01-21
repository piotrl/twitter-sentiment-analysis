import scrapper.Tweet;
import summary.DaySummary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class FileUtil {

    private final static String DELIMER = ";";

    public static void saveTweetsToCsv(List<Tweet> entries, String partyName) {
        saveToFile(partyName, buildCsv(entries, FileUtil::toCsvRow));
    }

    public static void saveSummaryToCsv(List<DaySummary> entries, String partyName) {
        String header = "DAY;HAPPINESS;ANGER;SADNESS;FEAR;DISGUST;UNCLASSIFIED\n";
        saveToFile("summary/" + partyName, header + buildCsv(entries, FileUtil::toCsvRow));
    }

    public static List<Tweet> loadFromCsv(String partyName) {
        List<String> csv = loadFromFile(partyName);
        return csv.stream()
                .map(FileUtil::csvToTweet)
                .collect(toList());
    }

    private static List<String> loadFromFile(String partyName) {
        try {
            return Files.readAllLines(Paths.get("./tweets/" + partyName + ".csv"));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static void saveToFile(String partyName, String content) {
        try {
            Files.write(Paths.get("./tweets/" + partyName + ".csv"), content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> String buildCsv(List<T> entries, Function<T, String> csvRow) {
        return entries.stream()
                .map(csvRow)
                .reduce((current, next) -> current + "\n" + next)
                .get();
    }

    private static Tweet csvToTweet(String csv) {
        String[] split = csv.split(DELIMER);
        String partyName = split[0];
        String dateText = split[1];
        String content = split[2];

        LocalDate date = LocalDate.parse(dateText, DateTimeFormatter.ISO_LOCAL_DATE);
        return new Tweet(partyName, date, content);
    }

    private static String toCsvRow(Tweet tweet) {
        return tweet.getPartyName() + DELIMER
                + tweet.getDay().format(DateTimeFormatter.ISO_LOCAL_DATE) + DELIMER
                + normalizeTweet(tweet.getTweet());
    }

    private static String toCsvRow(DaySummary summary) {
        return summary.getDay().format(DateTimeFormatter.ISO_LOCAL_DATE) + DELIMER
                + summary.getHAPPINESS() + DELIMER
                + summary.getANGER() + DELIMER
                + summary.getSADNESS() + DELIMER
                + summary.getFEAR() + DELIMER
                + summary.getDISGUST() + DELIMER
                + summary.getUNCLASSIFIED() + DELIMER
                ;
    }

    private static String normalizeTweet(String tweetText) {
        return tweetText
                .replaceAll("/.", "")
                .replaceAll(",", "")
                .replaceAll(";", "")
                .replaceAll("\n", "");
    }

}
