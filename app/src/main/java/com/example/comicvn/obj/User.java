package com.example.comicvn.obj;

public class User {
    private String id;
    private String username;
    private String password;
    private String name;
    private int age;
    private int role;

    public User(){}

    public User(String id, String username, String password, String name, int age, int role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.age = age;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getRole() {
        return role;
    }
}
