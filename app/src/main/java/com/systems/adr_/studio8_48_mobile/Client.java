package com.systems.adr_.studio8_48_mobile;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by luis_ on 29/04/2017.
 */

public class Client extends Person {
    private String phone;
    private boolean hasCredit;
    private Appointment newAppointment;
    private JSONArray appointments = new JSONArray();

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

    public Appointment getNewAppointment() {
        return newAppointment;
    }

    public void setNewAppointment(Appointment newAppointment) {
        this.newAppointment = newAppointment;
    }

    public JSONArray getAppointments() {
        return appointments;
    }

    public void setAppointments(JSONArray appointments) {
        this.appointments = appointments;
    }
}
