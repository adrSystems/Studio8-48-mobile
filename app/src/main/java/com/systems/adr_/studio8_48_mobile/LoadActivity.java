package com.systems.adr_.studio8_48_mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class LoadActivity extends AppCompatActivity {

    private Timer timer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timer.cancel();
                        Intent intent;
                        if(Auth.getInstance().getClient() == null){
                            intent = new Intent(LoadActivity.this,LoginActivity.class);
                        }
                        else {
                            intent = new Intent(LoadActivity.this, MainActivity.class);
                        }
                        (LoadActivity.this).startActivity(intent);
                        LoadActivity.this.finish();
                    }
                });
            }
        };

        timer.scheduleAtFixedRate(task,1500,1000);
    }
}
