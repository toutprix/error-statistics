import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Formatter;
import java.util.Map;

/**
 * Statistics writer.
 */
@Slf4j
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
                log.error("Не удалось создать дерикторию по указанному пути");
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
            for (Map.Entry<LocalDateTime, Integer> entries : statistics.entrySet()) {
                bufferedWriter.write(getResult(entries.getKey(), entries.getValue()));
            }
            log.info("Статистика создана");
        } catch (IOException e) {
            log.error("Ошибка при создании файла статистики");
        }
    }
    
    /**
     * Get string about date and count of error for writing.
     *
     * @param date date of error.
     * @param errorCount count of error.
     * @return formatter string.
     */
    private String getResult(LocalDateTime date, int errorCount) {
        int beginHour = date.getHour();
        int endHour = beginHour == 23 ? 0 : beginHour + 1;
        Formatter formatter = new Formatter();
        return formatter.format("%02d-%02d-%02d, %02d.00-%02d.00 Количество ошибок: %d\n", date.getYear(), date.getMonth().getValue(),
                                date.getDayOfMonth(), beginHour, endHour, errorCount).toString();
    }
}
