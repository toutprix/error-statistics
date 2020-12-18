
public class ErrorStatistics {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Не указаны пути к файлам");
            return;
        }
        
        String pathToLogFilesDir = args[0];
        String pathToStatisticsFile = args[1];
        ILogReader logReader = new LogReader(pathToLogFilesDir);
        StatisticsWriter statisticsWriter = new StatisticsWriter(logReader, pathToStatisticsFile);
        statisticsWriter.createStatistics();
    }
}
