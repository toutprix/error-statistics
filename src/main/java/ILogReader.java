import java.time.LocalDateTime;
import java.util.Map;

/**
 * Log reader interface.
 */
interface ILogReader {
    Map<LocalDateTime, Integer> readLogs();
}
