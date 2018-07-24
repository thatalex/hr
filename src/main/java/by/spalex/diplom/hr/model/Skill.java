package by.spalex.diplom.hr.model;

import by.spalex.diplom.hr.tools.Util;

import javax.persistence.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "skill")
public class Skill {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "name")
    private String name = "";

    @Column(name = "priority")
    private double priority = 0.0;

    @Column(name = "is_age")
    private Boolean ageFlag = false;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Value> values = new ArrayList<>();

    public Skill() {
    }

    public Skill(String skillName, double priority, String... names) {
        this.name = skillName;
        this.priority = priority;
        createValues(names);
    }

    public Skill(String skillName, double priority, int from, int to) {
        this.name = skillName;
        this.priority = priority;
        createValues(from, to);
    }

    private void createValues(String[] names) throws IllegalArgumentException {
        values.clear();
        for (String name : names) {
            if (Util.isEmpty(name)) {
                throw new IllegalArgumentException("Присутствует пустая строка: " + Arrays.toString(names));
            }
        }
        values.add(new Value(names[0].trim(), 0.0));
        double step = Util.round(1.0 / (names.length - 1), 3);
        if (step < 0.01) {
            step = 0.01;
        }
        double iter = step;
        if (names.length > 1) {
            for (int i = 1; i < names.length - 1; i++) {
                String name = names[i].trim();
                values.add(new Value(name, iter));
                iter += step;
            }
            values.add(new Value(names[names.length - 1].trim(), 1.0));
        }
    }

    private void createValues(int from, int to) {
        values.clear();
        values.add(new Value(String.valueOf(from), 0.0));
        double step = Util.round(1.0 / (Math.abs(to - from) + 1), 3);
        if (step < 0.001) {
            step = 0.001;
        }
        double iter = step;
        if (from < to) {
            for (int i = from + 1; i < to; i++) {
                values.add(new Value(String.valueOf(i), iter));
                iter += step;
            }
        } else {
            for (int i = from - 1; i > to; i--) {
                values.add(new Value(String.valueOf(i), iter));
                iter += step;
            }
        }
        values.add(new Value(String.valueOf(to), 1.0));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    /**
     * Parse raw value from string and create value's list
     *
     * @param rawValues string in one of format: set of values divided by comma (e.g.: "one, two, three") or
     *                  minimal and maximal integer values divided by dash (e.g.: "10-50")
     * @throws ParseException in case of invalid string format
     */
    public void parse(String rawValues) throws ParseException {
        String[] split = rawValues.split("-");
        if (split.length == 2) {
            try {
                int min = Integer.parseInt(split[0].trim());
                int max = Integer.parseInt(split[1].trim());
                if (min >= max) {
                    throw new ParseException("Начальное число больше конечного", 0);
                }
                createValues(min, max);
                return;
            } catch (NumberFormatException e) {
                throw new ParseException(e.toString(), 0);
            }
        } else {
            split = rawValues.split(",");
            if (split.length > 1) {
                try {
                    createValues(split);
                    return;
                } catch (IllegalArgumentException e) {
                    throw new ParseException(e.toString(), 0);
                }
            }
        }
        throw new ParseException("Неверный формат строки. Виды форматов: " +
                "список значений о младшего к старшему разделенные запятой (\"Один, Два, Три\") " +
                "или начальное и конечное число разделенные дефисом (пример: \"17-30\")", 0);
    }

    /**
     * generate values of skill to string
     */
    public String getRawValues() {
        if (values.size() == 0) {
            return "";
        }
        int[] ints = new int[values.size()];
        try {
            for (int i = 0; i < values.size(); i++) {
                ints[i] = Integer.parseInt(values.get(i).getName());
            }
            return ints[0] + " - " + ints[ints.length - 1];
        } catch (NumberFormatException e) {
            StringBuilder builder = new StringBuilder();
            for (Value value : values) {
                builder.append(value.getName()).append(", ");
            }
            builder.setLength(builder.length() - 2);
            return builder.toString();
        }
    }

    public boolean getAgeFlag() {
        return ageFlag != null ? ageFlag : false;
    }

    public boolean isAge(){
        return ageFlag != null ? ageFlag : false;
    }

    public void setAgeFlag(Boolean ageFlag) {
        this.ageFlag = ageFlag != null ? ageFlag : false;
    }
}
