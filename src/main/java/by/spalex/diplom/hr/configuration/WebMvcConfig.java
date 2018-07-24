package by.spalex.diplom.hr.configuration;

import by.spalex.diplom.hr.model.*;
import by.spalex.diplom.hr.service.PretenderService;
import by.spalex.diplom.hr.service.SkillService;
import by.spalex.diplom.hr.service.UserService;
import by.spalex.diplom.hr.tools.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Implementation of {@link WebMvcConfigurerAdapter}
 */
@Configuration
@EnableScheduling
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @org.springframework.beans.factory.annotation.Value("${spring.security.admin.name}")
    private String adminLogin;

    @org.springframework.beans.factory.annotation.Value("${spring.security.admin.password}")
    private String adminPass;

    private final SkillService skillService;

    private final PretenderService pretenderService;

    private final UserService userService;

    @Autowired
    public WebMvcConfig(SkillService skillService, PretenderService pretenderService, UserService userService) {
        this.skillService = skillService;
        this.pretenderService = pretenderService;
        this.userService = userService;
    }


    /**
     * After application startup checks database for existing Admin.
     * If Admin is not present then recreates sample data and store it to database
     */
    @PostConstruct
    protected void init() {
        User admin = userService.findOneByEmail(adminLogin);
        if (admin == null) {
            admin = new User();
            admin.setEmail(adminLogin);
            admin.setPassword(Util.encode(adminPass));
            admin.setRole(Role.ADMIN);
            admin.setName("ADMIN");
            userService.save(admin);

            User chief = new User();
            chief.setEmail("chief@localhost");
            chief.setPassword(Util.encode("123"));
            chief.setRole(Role.CHIEF);
            chief.setName("Сидоров Д.В");
            userService.save(chief);

            generateSkill();
            generateRandomData();
        }
        Skill ageSkill = skillService.getAgeSkill();
        if (ageSkill == null){
            Skill skillAge = new Skill("Возраст", 0.1, 80, 16);
            skillAge.setAgeFlag(true);
            skillService.save(skillAge);
        }
    }

    private void generateSkill() {
        skillService.save(new Skill("Образование", 0.2, "Образование", "Базовое",
                "Среднее", "Начальное-профессиональное", "Среднее-специальное", "Высшее"));
        skillService.save(new Skill("Техническое образование", 0.2, "Нет", "Да"));
        skillService.save(new Skill("Стаж работы", 0.2, 0, 48));
        skillService.save(new Skill("Опыт работы в розничной торговле", 0.15, 0, 48));
        skillService.save(new Skill("Знание MS Office", 0.05, "Нет", "Да"));
        skillService.save(new Skill("Знание английского языка", 0.1, "Базовое", "Среднее", "Продвинутое"));
    }

    private void generateRandomData() {
        Random random = new Random();
        List<Skill> skills = skillService.findAll();
        LocalDateTime dateTime = Util.now();
        for (int i = 1; i < 10; i++) {

            User user = new User();
            user.setEmail("user" + i + "@localhost");
            user.setPassword(Util.encode("123"));
            user.setRole(Role.PRETENDER);
            user.setName("Соискатель №" + i);
            userService.save(user);

            Pretender pretender = new Pretender();
            pretender.setUser(user);
            pretender.setAddress("Советская д. " + i);
            pretender.setPhone("555-44-4" + i);
            pretender.setBirthDate(LocalDate.from(dateTime.minus(random.nextInt(22000) + 6570, ChronoUnit.DAYS)));
            Set<SkillValue> skillValues = new HashSet<>();
            for (Skill skill : skills) {
                Value[] values = skill.getValues().toArray(new Value[0]);
                Value value = null;
                if (skill.getName().equalsIgnoreCase("Возраст")) {
                    String age = String.valueOf(ChronoUnit.YEARS.between(pretender.getBirthDate(), Util.now()));
                    for (Value value1 : values) {
                        if (value1.getName().equalsIgnoreCase(age)) {
                            value = value1;
                        }
                    }
                }
                if (value == null) {
                    value = values[random.nextInt(values.length - 1) + 1];
                }
                skillValues.add(new SkillValue(skill, value, pretender));
            }
            pretender.setSkillValues(skillValues);
            pretender.setUser(user);
            pretenderService.save(pretender);
        }
    }

}