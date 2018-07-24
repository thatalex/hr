package by.spalex.diplom.hr.service;

import by.spalex.diplom.hr.model.*;
import by.spalex.diplom.hr.repository.PretenderRepository;
import by.spalex.diplom.hr.repository.SkillRepository;
import by.spalex.diplom.hr.repository.SkillValueRepository;
import by.spalex.diplom.hr.repository.UserRepository;
import by.spalex.diplom.hr.tools.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class PretenderServiceImpl implements PretenderService {

    private static final String STR_AGE_RUSSIAN = "Возраст";
    private static final String STR_AGE = "age";
    private static final String MAIL_MESSAGE = "Уважаемый %s!\r\nПриглашаем Вас на собеседование. Наш офис находися по адресу %s. Время работы %s";
    private final PretenderRepository pretenderRepository;

    private final SkillRepository skillRepository;

    private final SkillValueRepository skillValueRepository;

    private final UserRepository userRepository;

    private final JavaMailSender mailSender;

    @org.springframework.beans.factory.annotation.Value("${hr.office.address}")
    private String officeAddress;
    @org.springframework.beans.factory.annotation.Value("${hr.office.worktime}")
    private String officeWorkTime;

    @Autowired
    public PretenderServiceImpl(PretenderRepository pretenderRepository, SkillRepository skillRepository,
                                SkillValueRepository skillValueRepository, UserRepository userRepository,
                                JavaMailSender mailSender) {
        this.pretenderRepository = pretenderRepository;
        this.skillRepository = skillRepository;
        this.skillValueRepository = skillValueRepository;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    @Override
    public Pretender getPretender(User user) {
        Pretender pretender = pretenderRepository.findOneByUser(user);
        if (pretender == null && user != null) {
            pretender = new Pretender();
            pretender.setUser(user);
            pretender.setAddress("");
            pretender.setPhone("");
            pretender.setBirthDate(LocalDate.from(Util.now().minus(18, ChronoUnit.YEARS)));
            setDefaultSkillValues(pretender);
            pretenderRepository.save(pretender);
        }
        return pretender;
    }

    @Override
    public Pretender getPretender(Long id) {
        return pretenderRepository.findOne(id);
    }

    private void setDefaultSkillValues(Pretender pretender) {
        Set<SkillValue> skillValues = new TreeSet<>();
        for (Skill skill : skillRepository.findAll()) {
            if (skill.getValues().isEmpty()) continue;
            Value[] values = skill.getValues().toArray(new Value[0]);
            if (STR_AGE_RUSSIAN.equalsIgnoreCase(skill.getName()) || STR_AGE.equalsIgnoreCase(skill.getName())) {
                String age = String.valueOf(ChronoUnit.YEARS.between(pretender.getBirthDate(), Util.now()));
                for (Value value : values) {
                    if (value.getName().equalsIgnoreCase(age)) {
                        skillValues.add(new SkillValue(skill, value, pretender));
                    }
                }
            } else {
                skillValues.add(new SkillValue(skill, values[0], pretender));
            }
        }
        pretender.setSkillValues(skillValues);
    }

    @Override
    public Pretender save(Pretender pretender) {
        pretender.syncBirthdaySkill();
        if (pretender.getId() > 0 && Util.isEmpty(pretender.getImage())) {
            Pretender oldpretender = pretenderRepository.findOne(pretender.getId());
            if (oldpretender != null) {
                pretender.setImage(oldpretender.getImage());
            }
        }
        Pretender result = pretenderRepository.save(pretender);
        skillValueRepository.deleteAllByPretenderIsNull();
        return result;
    }

    @Override
    public long count() {
        return pretenderRepository.count();
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        Pretender pretender = pretenderRepository.findOne(id);
        if (pretender != null) {
            pretenderRepository.delete(pretender);
            userRepository.delete(pretender.getUser());
            return true;
        }
        return false;
    }

    @Override
    public Pretender findOne(Long id) {
        return pretenderRepository.findOne(id);
    }

    @Override
    public boolean invite(Long id) {
        Pretender pretender = pretenderRepository.findOne(id);
        if (pretender != null) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(pretender.getUser().getEmail());
            message.setSubject("Собеседование");
            String builder = String.format(MAIL_MESSAGE, pretender.getUser().getName(), officeAddress, officeWorkTime);
            message.setText(builder);
            mailSender.send(message);
            pretender.setInvited(true);
            pretenderRepository.save(pretender);
            return true;
        }
        return false;
    }

    @Override
    public void deleteByUserId(Long id) {
        pretenderRepository.deleteByUserId(id);
    }

    @Override
    public List<Pretender> getSortedPretenders(Skill skill, String name, Integer age_from, Integer age_till) {
        List<Pretender> pretenders = new ArrayList<>();
        for (Pretender pretender : pretenderRepository.findAll()) {
            long age = pretender.getAge();
            if (skill == null || pretender.getSkillRate(skill) != null) {
                if (name == null || pretender.getUser().getName().toLowerCase().contains(name)) {
                    if (age_from == null || age > age_from) {
                        if (age_till == null || age < age_till) {
                            pretenders.add(pretender);
                        }
                    }
                }
            }
        }
        Util.sortBySkillRate(pretenders, skill);
        return pretenders;
    }

    @Override
    public List<Pretender> getSortedPretenders(Skill skill, String name) {
        List<Pretender> pretenders = new ArrayList<>();
        for (Pretender pretender : pretenderRepository.findAll()) {
            if (skill == null || pretender.getSkillRate(skill) != null) {
                if (name == null || pretender.getUser().getName().toLowerCase().contains(name)) {
                    pretenders.add(pretender);
                }
            }
        }
        if (skill != null) {
            Util.sortBySkillRate(pretenders, skill);
        } else {
            Collections.sort(pretenders);
        }
        return pretenders;
    }
}
