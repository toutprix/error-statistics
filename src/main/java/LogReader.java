import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * Log reader.
 */
@Slf4j
@RequiredArgsConstructor
class LogReader implements ILogReader {
    
    /** Date format. */
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    
    /** Type of error. */
    private static final String ERROR_TYPE = "ERROR";
    
    /** Statistics by date. */
    private Map<LocalDateTime, Integer> statistics;
    
    /** Path to dir with error log files. */
    @NonNull
    private final String pathToLogFilesDir;
    
    /**
     * Read log files.
     *
     * @return map with LocalDateTime and count of error.
     */
    @Override
    public Map<LocalDateTime, Integer> readLogs() {
        statistics = new TreeMap<>();
        Arrays.stream(getLogFiles()).forEach(this::readLogFile);
        return statistics;
    }
    
    /**
     * Get files name with the .log extension.
     *
     * @return array of files.
     */
    private File[] getLogFiles() {
        File filesDir = new File(pathToLogFilesDir);
        if (!filesDir.exists()) {
            log.error("Дериктории не существует");
            System.exit(0);
        }
        File[] listFiles = filesDir.listFiles((dir, name) -> name.endsWith(".log"));
        if (listFiles == null) {
            log.error("Не нейдено *.log файлов для чтения");
            System.exit(0);
        }
        return listFiles;
    }
    
    /**
     * Read log file.
     *
     * @param file file for read.
     */
    private void readLogFile(File file) {
        try (Stream<String> linesStream = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
            linesStream.forEach(this::parseLine);
        } catch (IOException e) {
            log.error("Ошибка при чтении из файла");
        }
    }
    
    /**
     * Parse line on date and type of error.
     *
     * @param line string for parsing.
     */
    private void parseLine(String line) {
        String[] parseLine = line.split(";");
        if (parseLine.length < 2) {
            return;
        }
        String date = parseLine[0];
        String type = parseLine[1];
        if (date == null || date.isEmpty() || !Objects.equals(ERROR_TYPE, type)) {
            return;
        }
        parseDate(date).ifPresent(result -> statistics.merge(result, 1, Integer::sum));
    }
    
    /**
     * Parse date.
     *
     * @param date string with date for parsing.
     * @return optional of LocalDateTime.
     */
    private Optional<LocalDateTime> parseDate(String date) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(DATE_FORMATTER.parse(date));
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        } catch (ParseException e) {
            log.error("Не удалось распознать дату: " + date);
            return Optional.empty();
        }
        return Optional.of(calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
    }
}
