package com.systems.adr_.studio8_48_mobile;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Hashtable;

public class DatePickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);
    }
    public void setDate(View v){
        DatePicker dp = (DatePicker)findViewById(R.id.datePickerNewAppointment);
        Hashtable<String,Integer> date = Auth.getInstance().getClient().getNewAppointment().getDate();
        date.put("day", dp.getDayOfMonth());
        date.put("month", dp.getMonth()+1);
        date.put("year", dp.getYear());
        Auth.getInstance().getClient().getNewAppointment().setDate(date);
        Auth.getInstance().getClient().getNewAppointment().dateText.setText(date.get("day")+"/"+date.get("month")+"/"+date.get("year"));
        finish();
    }
}
