package org.example.theadnan;

public class User {
    private final String email;
    private final String name;
    private final int age;
    private final String profession;
    private final String hobby;
    private final double balance;
    private final boolean isAdmin;
    private final boolean blocked;

    public User(String email, String name, int age, String profession, String hobby, double balance, boolean isAdmin, boolean blocked) {
        this.email = email;
        this.name = name;
        this.age = age;
        this.profession = profession;
        this.hobby = hobby;
        this.balance = balance;
        this.isAdmin = isAdmin;
        this.blocked = blocked;
    }

    public String getEmail() { return email; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getProfession() { return profession; }
    public String getHobby() { return hobby; }
    public double getBalance() { return balance; }
    public boolean isAdmin() { return isAdmin; }
    public boolean isBlocked() { return blocked; }
}