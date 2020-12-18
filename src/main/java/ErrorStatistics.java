import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorStatistics {
    public static void main(String[] args) {
        if (args.length < 2) {
            log.error("Не указаны пути к файлам");
            return;
        }
        
        String pathToLogFilesDir = args[0];
        String pathToStatisticsFile = args[1];
        ILogReader logReader = new LogReader(pathToLogFilesDir);
        StatisticsWriter statisticsWriter = new StatisticsWriter(logReader, pathToStatisticsFile);
        statisticsWriter.createStatistics();
    }
}
