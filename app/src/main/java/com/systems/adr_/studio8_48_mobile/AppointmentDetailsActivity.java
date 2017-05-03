package com.systems.adr_.studio8_48_mobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AppointmentDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

        int id = getIntent().getIntExtra("appointmentId",0);
        JSONArray apps = Auth.getInstance().getClient().getAppointments();
        JSONObject appointment = null;
        try{
            for (int i = 0; i < apps.length(); i++){
                if(((JSONObject)apps.get(i)).getInt("id") == id)
                {
                    appointment = (JSONObject)apps.get(i);
                    ((TextView)findViewById(R.id.tvDate)).setText(appointment.getString("fecha"));
                    ((TextView)findViewById(R.id.tvTime)).setText(appointment.getString("hora"));
                    ((TextView)findViewById(R.id.tvState)).setText(appointment.getString("estadoString"));
                    ((TextView)findViewById(R.id.tvMonto)).setText("$"+String.valueOf(appointment.getDouble("monto")));
                    JSONObject stylist = appointment.getJSONObject("empleado");
                    ((TextView)findViewById(R.id.tvStylistName)).setText(stylist.getString("nombre")+" "+stylist.getString("apellido"));
                    Picasso.with(this).load(stylist.getString("fotografia")).into(((ImageView)findViewById(R.id.ivStylistPhoto)));
                    JSONArray services = appointment.getJSONArray("servicios");
                    for (int x = 0; x < services.length(); x++){
                        TextView tv = new TextView(this);
                        tv.setTextColor(getResources().getColor(R.color.black3));
                        tv.setText(((JSONObject)services.get(x)).getString("nombre")+" ($"+((JSONObject)services.get(x)).getDouble("precioFinal")+")");
                        ((LinearLayout)findViewById(R.id.llServicesContainer)).addView(tv);
                    }
                }
            }
        } catch (Exception e){
            Toast.makeText(this, "Ha ocurrido un error...", Toast.LENGTH_SHORT).show();
        }
        ((Button)findViewById(R.id.btnBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
