package com.systems.adr_.studio8_48_mobile;

import java.util.Date;

/**
 * Created by luis_ on 29/04/2017.
 */

public class Person {
    private int id;
    private String name;
    private String lastName;
    private String birthday;
    private String registerDate;
    private Account account;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getCompleteName(){
        return getName()+" "+getLastName();
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
