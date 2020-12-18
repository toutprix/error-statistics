import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class StatisticsWriterTest {
    
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    
    @Test
    public void testWriteStatisticsFile() throws IOException {
        ILogReader logReader = new LogReader("src/test/resources/");
        StatisticsWriter statisticsWriter = new StatisticsWriter(logReader, tempFolder.getRoot().getAbsolutePath());
        statisticsWriter.createStatistics();
        List<String> strings = Files.readAllLines(Path.of(tempFolder.getRoot().getAbsolutePath() + "Statistics.txt"));
        MatcherAssert.assertThat(strings, CoreMatchers.hasItem("2019-10-10, 10.00-11.00 Количество ошибок: 2"));
        tempFolder.delete();
    }
}
