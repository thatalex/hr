package by.spalex.diplom.hr.service;

import by.spalex.diplom.hr.model.SkillValue;
import by.spalex.diplom.hr.repository.SkillValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillValueServiceImpl implements SkillValueService {

    private final SkillValueRepository skillValueRepository;

    @Autowired
    public SkillValueServiceImpl(SkillValueRepository skillValueRepository) {
        this.skillValueRepository = skillValueRepository;
    }

    @Override
    public List<SkillValue> findAll() {
        return skillValueRepository.findAll();
    }
}
