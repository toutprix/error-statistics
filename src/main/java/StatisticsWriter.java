import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Statistics writer.
 */
@RequiredArgsConstructor
class StatisticsWriter {
    
    /** Log reader. */
    @NonNull
    private ILogReader logReader;
    
    /** Path to directory, where needs to save the statistics file. */
    @NonNull
    private String pathToFile;
    
    /** Name of statistics file. */
    private static final String STATISTICS_FILE_NAME = "Statistics.txt";
    
    /**
     * Create statistic file.
     */
    void createStatistics() {
        Map<LocalDateTime, Integer> statistics = logReader.readLogs();
        if (statistics.isEmpty()) {
            return;
        }
        createDirectoriesToFileIfNotExists();
        writeStatistics(statistics);
    }
    
    /**
     * Create directories to statistics file if not exists.
     */
    private void createDirectoriesToFileIfNotExists() {
        Path directoryPath = Path.of(pathToFile);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                System.out.println("Не удалось создать дерикторию по указанному пути");
                System.exit(0);
            }
        }
    }
    
    /**
     * Write statistics in file.
     *
     * @param statistics map with LocalDateTime and count of errors.
     */
    private void writeStatistics(Map<LocalDateTime, Integer> statistics) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(pathToFile + STATISTICS_FILE_NAME), StandardCharsets.UTF_8))) {
            LocalDateTime date;
            for (Map.Entry<LocalDateTime, Integer> entries : statistics.entrySet()) {
                date = entries.getKey();
                int beginHour = date.getHour();
                String endHour = beginHour == 23 ? "00" : String.valueOf(beginHour + 1);
                bufferedWriter.write(
                        date.getYear() + "-" + date.getMonth().getValue() + "-" + date.getDayOfMonth() + ", " + beginHour + ".00-" + endHour + ".00 "
                                + "Количество " + "ошибок: " + entries.getValue() + "\n");
            }
            System.out.println("Статистика создана");
        } catch (IOException e) {
            System.out.println("Ошибка при создании файла статистики");
        }
    }
}
