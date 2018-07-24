package by.spalex.diplom.hr.repository;

import by.spalex.diplom.hr.model.Skill;
import by.spalex.diplom.hr.model.SkillValue;
import by.spalex.diplom.hr.model.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillValueRepository extends JpaRepository<SkillValue, Long> {
    List<SkillValue> findAllBySkillEquals(Skill skill);

    SkillValue findByValue(Value delValue);

    /**
     * remove orphans skill values
     */
    void deleteAllByPretenderIsNull();
}
