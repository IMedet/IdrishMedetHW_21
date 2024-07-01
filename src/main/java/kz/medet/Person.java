package kz.medet;

@AppTable(title = "persons")
public class Person {
    @AppField
    private int id;

    @AppField
    private String name;

    @AppField String surname;

    @AppField
    private String email;

    private int age;

}
