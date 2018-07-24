package by.spalex.diplom.hr.service;

import by.spalex.diplom.hr.model.Pretender;
import by.spalex.diplom.hr.model.Skill;
import by.spalex.diplom.hr.model.User;

import java.util.List;

public interface PretenderService {
    /**
     * Return {@link Pretender} instance by user if exists or create new instance with filled skills by default
     * @param user user for Pretender's instance lookup
     * @return Pretender instance linked to given user
     */
    Pretender getPretender(User user);

    /**
     * Return {@link Pretender} instance by identity if exists or create new instance with filled skills by default
     * @param id identity of Pretender
     * @return Pretender instance
     */
    Pretender getPretender(Long id);

    Pretender save(Pretender pretender);

    long count();

    /**
     * Get list of Pretenders sorted by skills rate
     * @param skill skill for sorting by rating. If skill is null - list is sorting by summary rating of all skills
     * @param name part of user's name. may be null
     * @param age_from minimal age of pretender
     * @param age_till maximum age of pretender
     * @return list of pretenders sorted by skills rate
     */
    List<Pretender> getSortedPretenders(Skill skill, String name, Integer age_from, Integer age_till);


    /**
     * Get list of Pretenders sorted by skills rate or by names
     * @param skill skill for sorting by rating. If skill is null - list is sorted by names
     * @param name part of user's name. may be null
     * @return list of pretenders sorted by skills
     */
    List<Pretender> getSortedPretenders(Skill skill, String name);


    boolean delete(Long id);

    Pretender findOne(Long id);

    boolean invite(Long id);


    void deleteByUserId(Long id);
}
