package org.example.theadnan;

public class User {
    private final String email;
    private final String name;
    private final int age;
    private final String profession;
    private final String hobby;

    public User(String email, String name, int age, String profession, String hobby) {
        this.email = email;
        this.name = name;
        this.age = age;
        this.profession = profession;
        this.hobby = hobby;
    }

    public String getEmail() { return email; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getProfession() { return profession; }
    public String getHobby() { return hobby; }
}