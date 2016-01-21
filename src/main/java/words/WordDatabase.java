package words;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class WordDatabase {
    private final static String DB_FILE = "nawl-analysis.csv";

    private final static int WORD_NAME = 0;
    private final static int EMOTION = 1;
    private final static int HAPINESS_INDEX = 2;
    private final static int ANGER_INDEX = 3;
    private final static int SADNESS_INDEX = 4;
    private final static int FEAR_INDEX = 5;
    private final static int DISGUST_INDEX = 6;

    public List<WordSemantic> importDb() {
        List<String> csv = importFromCsv();
        csv.remove(0); // title

        return csv.stream()
                .map(this::map)
                .collect(toList());
    }

    private WordSemantic map(String entry) {
        String[] attributes = entry.split(",");
        WordSemantic semantic = new WordSemantic();
        semantic.setName(attributes[WORD_NAME].replaceAll("\"", ""));
        semantic.setEmotion(emotion(attributes[EMOTION]));
        semantic.setAnger(Double.valueOf(attributes[ANGER_INDEX]));
        semantic.setHappiness(Double.valueOf(attributes[HAPINESS_INDEX]));
        semantic.setSadness(Double.valueOf(attributes[SADNESS_INDEX]));
        semantic.setFear(Double.valueOf(attributes[FEAR_INDEX]));
        semantic.setDisgust(Double.valueOf(attributes[DISGUST_INDEX]));

        return semantic;
    }

    private Emotion emotion(String attribute) {
        switch (attribute.trim()) {
            case "H":
                return Emotion.HAPPINESS;
            case "A":
                return Emotion.ANGER;
            case "D":
                return Emotion.DISGUST;
            case "F":
                return Emotion.FEAR;
            case "N":
                return Emotion.NEUTRAL;
            case "S":
                return Emotion.SADNESS;
            case "U":
                return Emotion.UNCLASSIFIED;
        }

        return Emotion.UNCLASSIFIED;
    }

    public List<String> importFromCsv() {
        URL url = getClass().getClassLoader().getResource(DB_FILE);

        try {
            Path path = Paths.get(url.toURI());
            return Files.readAllLines(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
