package by.spalex.diplom.hr.repository;


import by.spalex.diplom.hr.model.LogItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<LogItem, Long> {
}
