package com.systems.adr_.studio8_48_mobile;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class LoadActivity extends AppCompatActivity {

    private Timer timer = new Timer();
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        requestQueue = Volley.newRequestQueue(this);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timer.cancel();
                        Intent intent;
                        LocalDBHelper dbHelper = new LocalDBHelper(LoadActivity.this,"studio848",null,1);
                        Cursor cur = dbHelper.getReadableDatabase().query("auth",null, null,null,null,null,null);
                        if(cur.getCount() < 1){
                            intent = new Intent(LoadActivity.this,LoginActivity.class);
                            LoadActivity.this.startActivity(intent);
                            LoadActivity.this.finish();
                        }
                        else {
                            intent = new Intent(LoadActivity.this, MainActivity.class);
                            cur.moveToFirst();
                            getUser(cur);
                        }
                    }
                });
            }
        };

        timer.scheduleAtFixedRate(task,0,1000);
    }
    int attemptsCount = 0;
    private void getUser(Cursor cur){
        String url="http://studio8-48.esy.es/android/user/get-by-id/"
                +cur.getInt(0);
        final Cursor curFinal = cur;
        JsonObjectRequest requestS=new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.getBoolean("result")){
                                Client c = new Client();
                                JSONObject cuentaJSON = response.getJSONObject("cuenta");
                                c.setName(cuentaJSON.getJSONObject("cliente").getString("nombre"));
                                c.setLastName(cuentaJSON.getJSONObject("cliente").getString("apellido"));
                                c.setHasCredit(Boolean.parseBoolean(cuentaJSON.getJSONObject("cliente").getString("credito")));
                                c.setPhone(cuentaJSON.getJSONObject("cliente").getString("telefono"));
                                c.setBirthday(cuentaJSON.getJSONObject("cliente").getString("fecha_nacimiento"));
                                c.setRegisterDate(cuentaJSON.getJSONObject("cliente").getString("fecha_registro"));
                                c.setId(cuentaJSON.getJSONObject("cliente").getInt("id"));
                                Account account = new Account();
                                account.setId(cuentaJSON.getInt("id"));
                                account.setActive(Boolean.parseBoolean(cuentaJSON.getString("active")));
                                account.setEmail(cuentaJSON.getString("email"));
                                account.setHasFB(Boolean.parseBoolean(cuentaJSON.getString("fb")));
                                account.setPhoto(cuentaJSON.getString("photo"));
                                c.setAccount(account);
                                Auth.getInstance().setClient(c, LoadActivity.this, false);
                                Intent intent = new Intent(LoadActivity.this, MainActivity.class);
                                LoadActivity.this.startActivity(intent);
                                LoadActivity.this.finish();
                            }else{
                                Toast.makeText(LoadActivity.this, "Ha ocurrido un error!", Toast.LENGTH_SHORT).show();
                            }
                        }catch(Exception e){
                            Toast.makeText(LoadActivity.this, "Error en la respuesta del servidor!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoadActivity.this, "Error de conexión!", Toast.LENGTH_SHORT).show();
                        if(attemptsCount < 3)
                        {
                            attemptsCount++;
                            getUser(curFinal);
                        }
                        else{
                            finish();
                        }

                    }
                }
        );
        requestQueue.add(requestS);
    }
}
