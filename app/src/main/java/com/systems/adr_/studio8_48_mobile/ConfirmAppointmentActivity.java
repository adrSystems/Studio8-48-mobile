package com.systems.adr_.studio8_48_mobile;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class ConfirmAppointmentActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    Appointment appointment = Auth.getInstance().getClient().getNewAppointment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_appointment);


        Stylist stylist = appointment.getStylist();
        Picasso.with(this).load(stylist.getPhoto()).into((ImageView)findViewById(R.id.imageViewStylistPhoto));
        ((TextView)findViewById(R.id.tvStylistName)).setText(stylist.getCompleteName());
        String[] months = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
        ((TextView)findViewById(R.id.textViewAppDate)).setText(appointment.getDate().get("day")+"/"+months[appointment.getDate().get("month")-1]+"/"+appointment.getDate().get("year"));
        String minutes = "";
        if(appointment.getTime().get("minutes") < 10)
            minutes = "0"+appointment.getTime().get("minutes");
        else {
            minutes = String.valueOf(appointment.getTime().get("minutes"));
        }
        ((TextView)findViewById(R.id.textViewAppTime)).setText(appointment.getTime().get("hours")+":"+minutes);
        for (Object s : appointment.getServices()){
            JSONObject service = (JSONObject) s;
            TextView tv = new TextView(this);
            try {
                Double price = Double.parseDouble(service.getString("precio"));
                Double discount = Double.parseDouble(service.getString("descuento"));
                tv.setText(service.getString("nombre")+" = $"+(price - (price * discount)));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tv.setTextColor(getResources().getColor(R.color.dodgerblue,null));
                }
                else{
                    tv.setTextColor(getResources().getColor(R.color.dodgerblue));
                }
                ((LinearLayout)findViewById(R.id.llServicesContainer)).addView(tv);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            ((TextView)findViewById(R.id.tvAppAmount)).setText("$"+String.valueOf(appointment.getAmount()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestQueue = Volley.newRequestQueue(this);
    }

    public void programAppointment(View v) throws JSONException {
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("date", appointment.getDate().get("year")+"-"+appointment.getDate().get("month")+"-"+appointment.getDate().get("day"));
        jsonRequest.put("time",appointment.getTime().get("hours")+":"+appointment.getTime().get("minutes"));
        jsonRequest.put("stylistId",appointment.getStylist().getId());
        jsonRequest.put("clientId",Auth.getInstance().getClient().getId());
        JSONArray jArr = new JSONArray();
        for (Object o :appointment.getServices())
        {
            jArr.put(((JSONObject)o).get("id"));
        }
        jsonRequest.put("services",jArr);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, "http://studio8-48.esy.es/android/appointment/new/"+jsonRequest.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.getBoolean("result")){
                                ((RelativeLayout) findViewById(R.id.confirmAppContainer)).setVisibility(View.GONE);
                                ((RelativeLayout) findViewById(R.id.successAppContainer)).setVisibility(View.VISIBLE);
                            }
                            else{
                                Toast.makeText(ConfirmAppointmentActivity.this, response.getString("msg"),Toast.LENGTH_SHORT).show();
                            }
                        }catch(Exception e){
                            Toast.makeText(ConfirmAppointmentActivity.this, "Error en la respuesta del servidor.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ConfirmAppointmentActivity.this, "Error de conexión!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(request);
    }

    public void done(View v){
        finish();
    }
}
