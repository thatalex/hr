package by.spalex.diplom.hr.model;

public enum Role {
    PRETENDER("Соискатель"),
    CHIEF("Руководитель"),
    ADMIN("Администратор");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
