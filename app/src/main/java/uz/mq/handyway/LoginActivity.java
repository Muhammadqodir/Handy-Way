package uz.mq.handyway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.sql.Time;

public class LoginActivity extends AppCompatActivity {
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setActionBar();
        context = LoginActivity.this;
    }

    private void initViews(){
        if (!Utils.isOnline(context)){
            showOffline();
            return;
        }
        if (Utils.isLogin(context)){
            runApp();
        }else{
            initLogin();
        }
    }


    private void initLogin(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(500);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((LinearLayout) findViewById(R.id.login)).setVisibility(View.VISIBLE);

                    }
                });
            }
        }).start();
    }

    private void showOffline(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((LinearLayout) findViewById(R.id.login)).setVisibility(View.GONE);
                        ((LinearLayout) findViewById(R.id.notConnection)).setVisibility(View.VISIBLE);
                        ((ImageButton) findViewById(R.id.btnRefreash)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((LinearLayout) findViewById(R.id.notConnection)).setVisibility(View.GONE);
                                initViews();
                            }
                        });
                    }
                });
            }
        }).start();
    }

    private void runApp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(200);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(context, MainActivity.class));
                        LoginActivity.this.finish();
                    }
                });
            }
        }).start();
    }

    private void setActionBar(){
        getSupportActionBar().hide();
    }
}