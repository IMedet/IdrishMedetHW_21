package kz.medet;

@AppTable(title = "cats")
public class Cat {
    @AppField
    private int id;

    @AppField
    private String name;

    private String version;
}
