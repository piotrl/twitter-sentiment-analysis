import org.junit.Assert;
import org.junit.Test;
import words.WordDatabase;
import words.WordSemantic;

import java.util.List;

public class WordDatabaseTest {

    @Test
    public void importDatabaseFile() {
        WordDatabase wordDatabase = new WordDatabase();
        List<String> strings = wordDatabase.importFromCsv();

        Assert.assertFalse(strings.isEmpty());
    }

    @Test
    public void importDatabase() {
        WordDatabase wordDatabase = new WordDatabase();
        List<WordSemantic> semantics = wordDatabase.importDb();

        Assert.assertFalse(semantics.isEmpty());
    }
}
