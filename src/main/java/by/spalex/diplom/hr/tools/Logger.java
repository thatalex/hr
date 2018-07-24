package by.spalex.diplom.hr.tools;

import by.spalex.diplom.hr.model.LogItem;
import by.spalex.diplom.hr.service.LogService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;

/**
 * Utility class for read and store log's events
 */
public enum Logger {

    INSTANCE;

    private static final Comparator<LogItem> dateComparator = (o1, o2) -> o1 == null && o2 == null ? 1 :
            o1 == null ? -1 : o2 == null ? 1 : o2.getDate().compareTo(o1.getDate());

    private LogService logService;

    private void setService(LogService logService) {
        this.logService = logService;
    }

    /**
     * Autowire {@link LogService}
     */
    @Component
    public static class LoggerServiceInjector {

        private final LogService logService;

        public LoggerServiceInjector(LogService logService) {
            this.logService = logService;
        }

        @PostConstruct
        public void postConstruct() {
            INSTANCE.setService(logService);
        }
    }

    /**
     * Store log with {@link Level#INFO} level
     *
     * @param message log's message
     */
    public void info(String message) {
        logService.save(log(Level.INFO, message));
    }

    /**
     * Store log with {@link Level#SEVERE} level
     *
     * @param message log's message
     */
    public void error(String message) {
        logService.save(log(Level.SEVERE, message));
    }


    /**
     * Strore log in repository with given arguments
     *
     * @param level   log's level
     * @param message log's message
     * @return log item
     */
    private LogItem log(Level level, String message) {
        LogItem logItem = new LogItem();
        logItem.setInvoker("SERVER");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            logItem.setInvoker(authentication.getName());
        }
        logItem.setLevel(level.getName());
        logItem.setMessage(message);
        return logItem;
    }

    /**
     * Sort log's list by date
     * @param logItems list of logs
     */
    public static void sort(List<LogItem> logItems) {
        Collections.sort(logItems, dateComparator);
    }

}
