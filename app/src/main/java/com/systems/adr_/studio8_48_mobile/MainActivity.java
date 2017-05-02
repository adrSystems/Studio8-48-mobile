package com.systems.adr_.studio8_48_mobile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LinearLayout llNewAppointment;
    RequestQueue requestQueue;
    Hashtable<String, Integer> today = new Hashtable<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        requestQueue = Volley.newRequestQueue(this);

        ViewGroup vg = (ViewGroup)findViewById(R.id.linearMain);
        createOrShowNewAppointmentLayout(vg);

        Button appointmentsDetailsBtn = (Button)findViewById(R.id.button_newAppointmentsDetails);
        appointmentsDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAppointmentsDetails(v);
            }
        });

        Button changeDateBtn = (Button)findViewById(R.id.buttonChangeDateLauncher);
        changeDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DatePickerActivity.class);
                startActivity(intent);

            }
        });

        today.put("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        today.put("month", Calendar.getInstance().get(Calendar.MONTH)+1);
        today.put("year", Calendar.getInstance().get(Calendar.YEAR));
        today.put("hours", Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        today.put("minutes", Calendar.getInstance().get(Calendar.MINUTE));

        //da error al volver del link, parece que se debera de settear al cliente que se vuelve null
        Auth.getInstance().getClient().setNewAppointment(new Appointment());
        Hashtable<String,Integer> date = new Hashtable<String, Integer>();
        date.put("day",today.get("day"));
        date.put("month",today.get("month"));
        date.put("year",today.get("year"));
        Auth.getInstance().getClient().getNewAppointment().setDate(date);
        Hashtable<String,Integer> time = new Hashtable<String, Integer>();
        time.put("hours",today.get("hours"));
        time.put("minutes",today.get("minutes"));
        Auth.getInstance().getClient().getNewAppointment().setTime(time);

        Auth.getInstance().getClient().getNewAppointment().dateText = (TextView)findViewById(R.id.textViewAppointmnetDate);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        ImageView iv = (ImageView) findViewById(R.id.imageViewProfilePhoto);
        String photo = Auth.getInstance().getClient().getAccount().getPhoto();
        if(!photo.equals(null) && !photo.equals("null"))
        {
            Picasso.with(this).load(Auth.getInstance().getClient().getAccount().getPhoto()).into(iv);
        }
        else
        {
            iv.setImageResource(R.drawable.rsz_default_profile_photo);
        }
        ((TextView)findViewById(R.id.textViewUserName)).setText(Auth.getInstance().getClient().getCompleteName());
        ((TextView)findViewById(R.id.textViewEmail)).setText(Auth.getInstance().getClient().getAccount().getEmail());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            LocalDBHelper dbHelper = new LocalDBHelper(this,"studio848",null,1);
            dbHelper.getWritableDatabase().delete("auth","id is not null",null);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        ViewGroup vg = (ViewGroup)findViewById(R.id.linearMain);

        if (id == R.id.nav_my_appointments) {

        } else if (id == R.id.nav_new_appointment) {
            vg.removeAllViews();

            //cambiar esto, mas bien marcará comochecked a los botones de los servicios que esten en el arraylist
            ArrayList<Object> servicesList = Auth.getInstance().getClient().getNewAppointment().getServices();
            for (int i = 0; i < servicesList.size(); i++){
                servicesList.remove(i);
            }
            createOrShowNewAppointmentLayout(vg);
        } else if (id == R.id.nav_new_sale) {

        } else if (id == R.id.nav_purchased) {

        } else if (id == R.id.nav_web) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://studio8-48.esy.es"));
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createOrShowNewAppointmentLayout(ViewGroup vg){
        if(llNewAppointment == null){

            View v = getLayoutInflater().inflate(R.layout.new_appointment_layout,vg,true);
            llNewAppointment = (LinearLayout) ((LinearLayout)v).getChildAt(0);
            Calendar cal = Calendar.getInstance();
            ((TextView)findViewById(R.id.textViewAppointmnetDate)).setText(cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR));
        }
        else
        {
            vg.addView(llNewAppointment);
        }
        today.put("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        today.put("month", Calendar.getInstance().get(Calendar.MONTH)+1);
        today.put("year", Calendar.getInstance().get(Calendar.YEAR));
        today.put("hours", Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        today.put("minutes", Calendar.getInstance().get(Calendar.MINUTE));
        getServices();
        getStylists();
    }

    private void getServices(){
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, "http://studio8-48.esy.es/android/services/get-all", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray services = response.getJSONArray("services");
                            LinearLayout servicesContainer = (LinearLayout)llNewAppointment.findViewById(R.id.llServicesContainer);
                            servicesContainer.removeAllViews();
                            for (int i = 0; i < services.length(); i++){
                                View v = getLayoutInflater().inflate(R.layout.service_item_layout, servicesContainer,true);
                                LinearLayout item = (LinearLayout) ((LinearLayout) v).getChildAt(((LinearLayout)v).getChildCount()-1);
                                final JSONObject service = ((JSONObject)services.get(i));
                                Picasso.with(MainActivity.this).load(service.getString("icono")).into((ImageView) (item).findViewById(R.id.serviceCover));
                                ((TextView)item.findViewById(R.id.serviceName)).setText(service.getString("nombre"));
                                if(service.getDouble("descuento") == 0){
                                    ((TextView)item.findViewById(R.id.servicePrice)).setText("$"+service.getString("precio"));
                                }
                                else{
                                    ((TextView)item.findViewById(R.id.servicePrice)).setText("De $"+service.getString("precio")
                                            +" a $"+(Double.parseDouble(service.getString("precio")) - (Double.parseDouble(service.getString("precio")) * service.getDouble("descuento"))));
                                }

                                ((ToggleButton)item.findViewById(R.id.toggle_btn)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if(isChecked){
                                            Auth.getInstance().getClient().getNewAppointment().getServices().add(service);
                                        }
                                        else{
                                            Auth.getInstance().getClient().getNewAppointment().getServices().remove(service);
                                        }
                                    }
                                });

                            }
                        }catch(Exception e){
                            Toast.makeText(MainActivity.this, "Error en la respuesta del servidor.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error de conexión!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(request);
    }

    private void getStylists(){
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, "http://studio8-48.esy.es/android/stylists/get-all", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray stylists = response.getJSONArray("stylists");
                            final LinearLayout stylistsContainer = (LinearLayout)llNewAppointment.findViewById(R.id.llStylistsContainer);
                            stylistsContainer.removeAllViews();
                            for (int i = 0; i < stylists.length(); i++){
                                View v = getLayoutInflater().inflate(R.layout.stylist_item_layout, stylistsContainer, true);
                                LinearLayout item = (LinearLayout) ((LinearLayout) v).getChildAt(((LinearLayout)v).getChildCount()-1);
                                final JSONObject stylist = ((JSONObject)stylists.get(i));
                                Picasso.with(MainActivity.this).load(stylist.getString("fotografia")).into((ImageView) (item).findViewById(R.id.stylistPhoto));
                                ((TextView)item.findViewById(R.id.stylistNombre)).setText(stylist.getString("nombre")+" "+stylist.getString("apellido"));

                                ((ToggleButton)item.findViewById(R.id.toggle_btn)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if(isChecked){
                                            for(int i = 0; i < stylistsContainer.getChildCount(); i++){
                                                ToggleButton tb = (ToggleButton)stylistsContainer.getChildAt(i).findViewById(R.id.toggle_btn);
                                                if(!tb.equals((ToggleButton)buttonView)){
                                                    tb.setChecked(false);
                                                }
                                            }
                                            Stylist s = new Stylist();
                                            try {
                                                s.setName(stylist.getString("nombre"));
                                                s.setLastName(stylist.getString("apellido"));
                                                s.setId(stylist.getInt("id"));
                                                s.setPhoto(stylist.getString("fotografia"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            Auth.getInstance().getClient().getNewAppointment().setStylist(s);
                                        }
                                        else{
                                            Auth.getInstance().getClient().getNewAppointment().setStylist(null);
                                        }
                                    }
                                });

                            }
                        }catch(Exception e){
                            Toast.makeText(MainActivity.this, "Error en la respuesta del servidor.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error de conexión!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(request);
    }

    private void viewAppointmentsDetails(View v){
        Hashtable<String,Integer> date = Auth.getInstance().getClient().getNewAppointment().getDate();
        Hashtable<String,Integer> time = Auth.getInstance().getClient().getNewAppointment().getTime();
        boolean fechaEsInferior = date.get("day") < today.get("day")
                || date.get("month") < today.get("month")
                || date.get("year") < today.get("year");
        boolean esHoy = date.get("day") == today.get("day")
                && date.get("month") == today.get("month")
                && date.get("year") == today.get("year");
        TimePicker tp = (TimePicker)findViewById(R.id.timePickerNewAppointment);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            time.put("minutes",tp.getMinute());
        }
        else {
            time.put("minutes",tp.getCurrentMinute());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            time.put("hours",tp.getHour());
        }
        else{
            time.put("hours",tp.getCurrentHour());
        }
        today.put("minutes",Calendar.getInstance().get(Calendar.MINUTE));
        today.put("hours",Calendar.getInstance().get(Calendar.HOUR_OF_DAY));

        if(fechaEsInferior){
            Toast.makeText(this, "La fecha no puede ser anterior a hoy", Toast.LENGTH_SHORT).show();
        }
        else if(esHoy && (time.get("hours") < today.get("hours")))
        {
            Toast.makeText(this, "La hora no puede ser inferior a la hora actual", Toast.LENGTH_SHORT).show();
        }
        else if(esHoy && (today.get("hours") == time.get("hours") && time.get("minutes") < today.get("minutes"))){
            Toast.makeText(this, "La hora es inferior a la hora actual por unos minutos", Toast.LENGTH_SHORT).show();
        }
        else if(Auth.getInstance().getClient().getNewAppointment().getServices().size() < 1){
            Toast.makeText(this, "Debe seleccionar al menos un servicio.", Toast.LENGTH_SHORT).show();
        }
        else if(Auth.getInstance().getClient().getNewAppointment().getStylist() == null){
            Toast.makeText(this, "Debe seleccionar un estilista.", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(this, ConfirmAppointmentActivity.class);
            startActivity(intent);
        }
    }
}
