package by.spalex.diplom.hr.service;

import by.spalex.diplom.hr.model.Skill;

import java.text.ParseException;
import java.util.List;

public interface SkillService {
    List<Skill> findAll();

    Skill getOrCreate(Long id);

    boolean delete(Long id);

    /**
     * Save skill with given priority and values
     * @param skill Skill to save
     * @param priority priority of skill in percents
     * @param rawValues list of values from min to max divided by comma (e.g.: One, Two, Three)
     *                  or first and last numbers divided by hyphen (e.g.: 17-30)
     * @return saved skill
     * @throws ParseException in case of rawValues can't be parsed
     */
    Skill save(Skill skill, int priority, String rawValues) throws ParseException;

    void save(Skill skill);

    Skill getAgeSkill();

}
