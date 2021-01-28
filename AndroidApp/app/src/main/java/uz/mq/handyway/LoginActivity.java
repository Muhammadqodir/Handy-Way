package uz.mq.handyway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONObject;

import java.sql.Time;

public class LoginActivity extends AppCompatActivity {
    Context context;
    HandyWayAPI handyWayAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setActionBar();
        context = LoginActivity.this;
        initViews();
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
                        ((Button) findViewById(R.id.btnLogin)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                login();
                            }
                        });
                    }
                });
            }
        }).start();
    }

    private void login(){
        ((Button) findViewById(R.id.btnLogin)).setVisibility(View.GONE);
        ((ProgressBar) findViewById(R.id.progress)).setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                handyWayAPI = new HandyWayAPI("");
                String tel = ((EditText) findViewById(R.id.etTel)).getText().toString();
                String pass = ((EditText) findViewById(R.id.etPass)).getText().toString();
                final APIResponse response = handyWayAPI.logIn(tel, pass);
                if (response.getCode() > 0){
                    if (response.getCode() == 1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.setLogin(context, true);
                                JSONObject userData = (JSONObject) response.getRes();
                                try {
                                    Utils.setUserToken(context, userData.getString("token"));
                                    Utils.setUserData(context, userData.getString("name"), userData.getString("phone_number"));
                                    ((LinearLayout) findViewById(R.id.login)).setVisibility(View.GONE);
                                    ((Button) findViewById(R.id.btnLogin)).setVisibility(View.VISIBLE);
                                    ((ProgressBar) findViewById(R.id.progress)).setVisibility(View.GONE);
                                    runApp();
                                }catch (Exception e){
                                    Utils.showErrorDialog(context, e.getMessage(), getLayoutInflater());
                                }
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((Button) findViewById(R.id.btnLogin)).setVisibility(View.VISIBLE);
                                ((ProgressBar) findViewById(R.id.progress)).setVisibility(View.GONE);
                                Toast.makeText(context, R.string.invalid_tel_pass, Toast.LENGTH_SHORT).show();
                                ((Button) findViewById(R.id.btnSupportPass)).setVisibility(View.VISIBLE);
                                ((Button) findViewById(R.id.btnSupportPass)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Utils.startSupportIntent(context);
                                    }
                                });
                            }
                        });
                    }
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((Button) findViewById(R.id.btnLogin)).setVisibility(View.VISIBLE);
                            ((ProgressBar) findViewById(R.id.progress)).setVisibility(View.GONE);
                            Utils.showErrorDialog(context, response.getMessage(), getLayoutInflater());
                        }
                    });
                }
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
                handyWayAPI = new HandyWayAPI(Utils.getUserToken(context));
                final APIResponse response = handyWayAPI.checkIsActive();
                if (response.getCode() > 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (response.getCode()){
                                case 1:
                                    if ((Boolean) response.getRes()){
                                        startActivity(new Intent(context, MainActivity.class));
                                        LoginActivity.this.finish();
                                    }else{
                                        ((LinearLayout) findViewById(R.id.blockedUser)).setVisibility(View.VISIBLE);
                                        ((Button) findViewById(R.id.btnSupport)).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Utils.startSupportIntent(context);
                                            }
                                        });
                                    }
                                    break;
                                case 2:
                                    Utils.setLogin(context, false);
                                    initViews();
                                    break;
                            }
                        }
                    });
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.showErrorDialog(context, response.getMessage(), getLayoutInflater());
                        }
                    });
                }
            }
        }).start();
    }

    private void setActionBar(){
        getSupportActionBar().hide();
    }
}