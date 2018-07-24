package by.spalex.diplom.hr.service;

import by.spalex.diplom.hr.model.LogItem;

import java.util.List;

public interface LogService {
    List<LogItem> findAll();

    void save(LogItem logItem);
}
