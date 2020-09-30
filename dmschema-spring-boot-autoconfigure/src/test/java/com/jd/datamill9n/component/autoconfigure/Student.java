package com.jd.datamill9n.component.autoconfigure;

import java.io.Serializable;

/**
 * TODO
 *
 * @author maliang56
 * @date 2020-09-29
 */
public class Student implements Serializable {
    private int id;
    private String name;
    private int age;
    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
