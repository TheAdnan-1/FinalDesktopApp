package org.example.theadnan;

public class User {
    private final String email;
    private final String name;
    private final int age;
    private final String profession;
    private final String hobby;
    private final double balance;

    public User(String email, String name, int age, String profession, String hobby, double balance) {
        this.email = email;
        this.name = name;
        this.age = age;
        this.profession = profession;
        this.hobby = hobby;
        this.balance = balance;
    }

    public String getEmail() { return email; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getProfession() { return profession; }
    public String getHobby() { return hobby; }
    public double getBalance() { return balance; }
}