package com.systems.adr_.studio8_48_mobile;

/**
 * Created by luis_ on 29/04/2017.
 */

public class Account {
    private int id;
    private String email;
    private String password;
    private boolean isActive;
    private boolean hasFB;
    private String photo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isHasFB() {
        return hasFB;
    }

    public void setHasFB(boolean hasFB) {
        this.hasFB = hasFB;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
