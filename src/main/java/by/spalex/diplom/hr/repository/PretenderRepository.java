package by.spalex.diplom.hr.repository;

import by.spalex.diplom.hr.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PretenderRepository extends JpaRepository<Pretender, Long> {
    Pretender findBySkillValuesEquals(SkillValue skillValue);

    Pretender findOneByUser(User user);

    void deleteByUserId(long id);
}
