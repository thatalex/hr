package by.spalex.diplom.hr.model;

import javax.persistence.*;

@Entity
@Table(name = "skill_value")
public class SkillValue implements Comparable<SkillValue> {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    private Skill skill;

    @ManyToOne(fetch = FetchType.EAGER)
    private Value value;

    @ManyToOne(fetch = FetchType.EAGER)
    private Pretender pretender;

    public SkillValue() {
    }

    public SkillValue(Skill skill, Value value, Pretender pretender) {
        this.skill = skill;
        this.value = value;
        this.pretender = pretender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public String getValueName() {
        return value.getName();
    }

    public double getRate() {
        return value.getRate();
    }

    public Pretender getPretender() {
        return pretender;
    }

    public void setPretender(Pretender pretender) {
        this.pretender = pretender;
    }

    @Override
    public int compareTo(SkillValue o) {
        return o == null ? -1 : skill.getName().compareTo(o.getSkill().getName());
    }
}
