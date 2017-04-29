package com.systems.adr_.studio8_48_mobile;

/**
 * Created by luis_ on 29/04/2017.
 */

public class Client extends Person {
    private String phone;
    private boolean hasCredit;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isHasCredit() {
        return hasCredit;
    }

    public void setHasCredit(boolean hasCredit) {
        this.hasCredit = hasCredit;
    }
}
