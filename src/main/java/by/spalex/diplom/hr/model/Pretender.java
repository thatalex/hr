package by.spalex.diplom.hr.model;

import by.spalex.diplom.hr.tools.Util;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Entity
@Table(name = "pretender")
public class Pretender implements Comparable<Pretender> {

    private static final String PHONE_PATTERN = "[+\\d]{0,1}[(]{0,1}[ \\d\\-]{2,6}[)]{0,1}[\\d\\-]+";
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "address")
    private String address = "";

    @Column(name = "phone")
    private String phone = "";

    @Column(name = "birth_date")
    @DateTimeFormat(pattern = Util.DATE_PATTERN)
    private LocalDate birthDate = LocalDate.from(Util.now().minus(18, ChronoUnit.YEARS));

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<SkillValue> skillValues = new TreeSet<>();

    @Column(name = "image")
    @Lob
    private String image = null;

    @Column(name = "invited")
    private Boolean invited = Boolean.FALSE;

    @OneToOne(fetch = FetchType.EAGER)
    private User user;

    public Pretender() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<SkillValue> getSkillValues() {
        return new TreeSet<>(skillValues);
    }

    public void setSkillValues(Set<SkillValue> skillValues) {
        this.skillValues = skillValues;
        for (SkillValue skillValue : skillValues) {
            skillValue.setPretender(this);
        }
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;

    }

    /**
     * set age skill equaled to birthday
     */
    public void syncBirthdaySkill(){
        for (SkillValue skillValue: skillValues) {
            Skill skill = skillValue.getSkill();
            if (skill.getAgeFlag()){
                String age = String.valueOf(ChronoUnit.YEARS.between(birthDate, Util.now()));
                for (Value value1 : skill.getValues()) {
                    if (value1.getName().equalsIgnoreCase(age)) {
                        skillValue.setValue(value1);
                    }
                }
            }
        }
    }

    /**
     * return summary rating of skills given an argument
     */
    public double getRate(Set<Skill> requiredSkills) {
        double rate = 0;
        for (Skill reuqiredSkill : requiredSkills) {
            Double value = getSkillRate(reuqiredSkill);
            if (value != null) {
                rate += value * reuqiredSkill.getPriority();
            }
        }
        return rate;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setInvited(Boolean invited) {
        this.invited = invited != null ? invited : false;
    }

    public Boolean getInvited() {
        return invited != null ? invited : false;
    }

    @Override
    public int compareTo(Pretender o) {
        return o == null ? -1 : this == o ? 0 : user.getName().compareTo(o.user.getName());
    }

    /**
     * get rating of given skill
     */
    public Double getSkillRate(Skill skill) {
        for (SkillValue skillValue : skillValues) {
            if (skillValue.getSkill().equals(skill)) {
                return skillValue.getRate();
            }
        }
        return null;
    }

    /**
     * return rating of all pretender's skills
     */
    public Double getSkillRates() {
        double result = 0;
        for (SkillValue skillValue : skillValues) {
            result += skillValue.getRate() * skillValue.getSkill().getPriority();
        }
        return result;

    }

    public long getAge() {
        return ChronoUnit.YEARS.between(birthDate, Util.now());
    }

    public String getStringAge() {
        String age = String.valueOf(getAge());
        char lastChar = age.charAt(age.length() - 1);
        switch (lastChar) {
            case '1':
                age += " год";
                break;
            case '2':
                age += " года";
                break;
            case '3':
                age += " года";
                break;
            case '4':
                age += " года";
                break;
            default:
                age += " лет";
                break;
        }
        return age;
    }

    public void removeSkillValue(SkillValue skillValue) {
        skillValues.remove(skillValue);
    }

    public boolean phoneIsValid() {
        return phone.matches(PHONE_PATTERN);
    }
}
