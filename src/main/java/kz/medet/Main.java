package kz.medet;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println(prepareCreateTableSQL(Person.class));
        System.out.println(prepateINsertIntoSQL(Person.class, 1,"Medet", "Idrish", "medet@mail.ru"));
        System.out.println(prepareALTERTABLESQL(Person.class,"address","String"));
        System.out.println(prepareSELECTSQL(Cat.class));
        System.out.println(prepareDROPTABLESQL(Person.class));
    }

    private static String prepareDROPTABLESQL(Class cl){
        //DROP TABLE table_name;
        if(!cl.isAnnotationPresent(AppTable.class)){
            throw new IllegalArgumentException("Класс не предназначен для создания ALTER TABLE ");
        }

        StringBuilder builder = new StringBuilder("DROP TABLE ");
        builder.append(((AppTable)cl.getAnnotation(AppTable.class)).title()).append(";");

        return builder.toString();
    }

    private static String prepareSELECTSQL(Class cl){
        // SELECT * FROM Customer;
        if(!cl.isAnnotationPresent(AppTable.class)){
            throw new IllegalArgumentException("Класс не предназначен для создания ALTER TABLE ");
        }

        StringBuilder builder = new StringBuilder("SELECT * FROM ");
        builder.append(((AppTable)cl.getAnnotation(AppTable.class)).title()).append(";");

        return builder.toString();
    }

    private static String prepareALTERTABLESQL(Class cl, String column, String type){
//        ALTER TABLE table_name
//        ADD column_name datatype;

        if(!cl.isAnnotationPresent(AppTable.class)){
            throw new IllegalArgumentException("Класс не предназначен для создания ALTER TABLE ");
        }

        Map<String , String> typeMapper = new HashMap<>();
        typeMapper.put("String","TEXT");
        typeMapper.put("int", "INTEGER");


        StringBuilder builder = new StringBuilder("ALTER TABLE ");

        builder.append(((AppTable)cl.getAnnotation(AppTable.class)).title()).append("\n");
        builder.append("ADD ").append(column).append(" ").append(typeMapper.get(type)).append(";");

        return builder.toString();
    }

    private static String prepateINsertIntoSQL(Class cl, int id, String name, String surname, String email){
        if (!cl.isAnnotationPresent(AppTable.class)){
            throw new IllegalArgumentException("Класс не предназначен для создания INSERT INTO ");
        }

//        INSERT INTO table_name (column1, column2, column3, ...)
//        VALUES (value1, value2, value3, ...);

        StringBuilder builder = new StringBuilder("INSERT INTO ");
        builder.append(((AppTable)cl.getAnnotation(AppTable.class)).title()).append(" (");
        //INSERT INTO table_name (

        Field[] fields = cl.getDeclaredFields();

        for (Field f : fields){
            if(f.isAnnotationPresent(AppField.class)){
                builder.append(f.getName()).append(", ");
            }
        }
        //INSERT INTO table_name (column1, column2, column3,

        builder.setLength(builder.length() - 2);
        builder.append(")\n");
        builder.append("VALUES (").append(id + ", ").append(name+ ", ").append(surname + ", ").append(email+");");

        return builder.toString();
    }

    private static String prepareCreateTableSQL(Class cl){

        if (!cl.isAnnotationPresent(AppTable.class)){
            throw new IllegalArgumentException("Класс не предназначен для создания Create Table ");
        }


        // CREATE TABLE cats (id INTEGER, name TEXT);

        Map<Class, String> typeMapper = new HashMap<>();
        typeMapper.put(String.class, "TEXT");
        typeMapper.put(int.class,"INTEGER");

        StringBuilder builder = new StringBuilder("CREATE TABLE ");

        builder.append(((AppTable)cl.getAnnotation(AppTable.class)).title()).append(" (");

        Field[] fields = cl.getDeclaredFields();

        for (Field f : fields){
                if(f.isAnnotationPresent(AppField.class)){
                    builder.append(f.getName()).append(" ").append(typeMapper.get(f.getType())).append(", ");
                }
        }

        builder.setLength(builder.length() - 2);

        builder.append(");");



        return builder.toString();
    }
}
