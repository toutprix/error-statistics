import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Map;

public class LogReaderTest {

    @Test
    public void testReadLogFiles() {
        ILogReader logReader = new LogReader("src/test/resources/");
        Map<LocalDateTime, Integer> localDateTimeIntegerMap = logReader.readLogs();
        int errorCount = localDateTimeIntegerMap.values().stream().reduce(Integer::sum).orElse(0);
        Assert.assertEquals(12, errorCount);
    }
}
