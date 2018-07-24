package by.spalex.diplom.hr.service;

import by.spalex.diplom.hr.model.LogItem;
import by.spalex.diplom.hr.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    @Autowired
    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public List<LogItem> findAll() {
        return logRepository.findAll();
    }

    @Override
    public void save(LogItem logItem) {
        logRepository.save(logItem);
    }
}
