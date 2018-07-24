package by.spalex.diplom.hr.service;

import by.spalex.diplom.hr.model.Pretender;
import by.spalex.diplom.hr.model.Skill;
import by.spalex.diplom.hr.model.SkillValue;
import by.spalex.diplom.hr.model.Value;
import by.spalex.diplom.hr.repository.PretenderRepository;
import by.spalex.diplom.hr.repository.SkillRepository;
import by.spalex.diplom.hr.repository.SkillValueRepository;
import by.spalex.diplom.hr.repository.ValueRepository;
import by.spalex.diplom.hr.tools.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    private final SkillValueRepository skillValueRepository;

    private final ValueRepository valueRepository;

    private final PretenderRepository pretenderRepository;

    @Autowired
    public SkillServiceImpl(SkillRepository skillRepository, SkillValueRepository skillValueRepository,
                            ValueRepository valueRepository, PretenderRepository pretenderRepository) {
        this.skillRepository = skillRepository;
        this.skillValueRepository = skillValueRepository;
        this.valueRepository = valueRepository;
        this.pretenderRepository = pretenderRepository;
    }

    @Override
    public List<Skill> findAll() {
        return skillRepository.findAll();
    }

    @Override
    public Skill getOrCreate(Long id) {
        Skill skill;
        if (id != null && id > 0) {
            skill = skillRepository.findOne(id);
        } else {
            skill = new Skill();
        }
        return skill;
    }

    @Override
    public boolean delete(Long id) {
        Skill skill = skillRepository.findOne(id);
        if (skill != null && !skill.getAgeFlag()) {
            List<SkillValue> skillValues = skillValueRepository.findAllBySkillEquals(skill);
            List<Pretender> pretenders = new ArrayList<>();
            for (SkillValue skillValue : skillValues) {
                Pretender pretender = pretenderRepository.findBySkillValuesEquals(skillValue);
                if (pretender != null) {
                    pretender.removeSkillValue(skillValue);
                    pretenders.add(pretender);
                    skillValue.setSkill(null);
                    skillValue.setValue(null);
                }
            }
            valueRepository.delete(skill.getValues());
            skillRepository.delete(skill);
            skillValueRepository.delete(skillValues);
            pretenderRepository.save(pretenders);
            return true;
        }
        return false;
    }

    @Override
    public Skill save(Skill skill, int priority, String rawValues) throws ParseException {
        skill.setPriority(Util.round(priority / 100.0, 2));
        //lookup for values names and renew that values instead creating new
        if (skill.getId() != null && skill.getId() > 0){
            Skill temp = skillRepository.findOne(skill.getId());
            if (temp != null){
                List<Value> oldValues = temp.getValues();
                skill.parse(rawValues);
                List<Value> deleteValues = new ArrayList<>();
                for (Value oldValue: oldValues){
                    boolean isExists = false;
                    for (Value newValue: skill.getValues()){
                        if (newValue.getName().equalsIgnoreCase(oldValue.getName())){
                            newValue.setId(oldValue.getId());
                            isExists = true;
                            break;
                        }
                    }
                    if (!isExists){
                        deleteValues.add(oldValue);
                    }
                }
                valueRepository.save(skill.getValues());
                for (Value delValue: deleteValues){
                    SkillValue skillValue = skillValueRepository.findByValue(delValue);
                    skillValue.setValue(skill.getValues().get(0));
                    skillValueRepository.save(skillValue);
                }
            }
        } else {
            skill.parse(rawValues);
        }
        return skillRepository.save(skill);
    }

    @Override
    public void save(Skill skill) {
        skillRepository.save(skill);
    }

    @Override
    public Skill getAgeSkill() {
        return skillRepository.findFirstByAgeFlagIsTrue();
    }
}
