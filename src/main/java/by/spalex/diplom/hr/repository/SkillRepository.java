package by.spalex.diplom.hr.repository;

import by.spalex.diplom.hr.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    Skill findFirstByAgeFlagIsTrue();
}
