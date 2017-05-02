package com.systems.adr_.studio8_48_mobile;

import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;

/**
 * Created by luis_ on 01/05/2017.
 */

public class Appointment {
    private ArrayList services = new ArrayList();
    private Hashtable<String, Integer> date;
    private Hashtable<String, Integer> time;
    private Stylist stylist;
    public TextView dateText;

    private double getAmount() throws JSONException {
        double sum = 0;
        for (Object s: services) {
            JSONObject service = (JSONObject)s;
            sum += service.getDouble("precio") - (service.getDouble("precio") * service.getDouble("descuento"));
        }
        return sum;
    }

    public ArrayList getServices() {
        return services;
    }

    public void setServices(ArrayList services) {
        this.services = services;
    }

    public Hashtable<String, Integer> getDate() {
        return date;
    }

    public void setDate(Hashtable<String, Integer> date) {
        this.date = date;
    }

    public Hashtable<String, Integer> getTime() {
        return time;
    }

    public void setTime(Hashtable<String, Integer> time) {
        this.time = time;
    }

    public Stylist getStylist() {
        return stylist;
    }

    public void setStylist(Stylist stylist) {
        this.stylist = stylist;
    }
}
