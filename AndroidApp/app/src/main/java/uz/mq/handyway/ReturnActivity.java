package uz.mq.handyway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import uz.mq.handyway.Adapters.OrderAdapter;
import uz.mq.handyway.Models.OrderModel;

public class ReturnActivity extends AppCompatActivity {

    Context context;
    HandyWayAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);
        context = this;
        api = new HandyWayAPI(Utils.getUserToken(context));
        setActionBar();
        initViews();
    }

    RecyclerView recyclerView;
    OrderAdapter adapter;
    private void initViews(){
        recyclerView = (RecyclerView) findViewById(R.id.cartList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fillList();
    }


    private void fillList(){
        isLoading(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final APIResponse response = api.getApprovedOrders();
                if (response.getCode() > 0){
                    switch (response.getCode()){
                        case 1:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isLoading(false);
                                    ArrayList<OrderModel> orderModels = (ArrayList<OrderModel>) response.getRes();
                                    if (orderModels.size() > 0){
                                        isEmpty(false);
                                        adapter = new OrderAdapter(context, orderModels, true, new Runnable() {
                                            @Override
                                            public void run() {
                                                getSupportActionBar().hide();
                                                ((LinearLayout) findViewById(R.id.llSuccess)).setVisibility(View.VISIBLE);
                                                ((Button) findViewById(R.id.btnBackToMain)).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        startActivity(new Intent(context, MainActivity.class));
                                                        ((Activity) context).finish();
                                                        ((Activity) context).finishAffinity();
                                                    }
                                                });
                                            }
                                        });
                                        recyclerView.setAdapter(adapter);
                                    }else {
                                        isEmpty(true);
                                    }
                                }
                            });
                            break;
                        case 2:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isLoading(false);
                                    Utils.logOut(context);
                                }
                            });
                            break;
                    }
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isLoading(false);
                            isEmpty(true);
                            Utils.showErrorDialog(context, response.getMessage(), getLayoutInflater());
                        }
                    });
                }
            }
        }).start();
    }

    private void setActionBar(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_icons8_left_4));
        setTitle(getResources().getString(R.string.select_order));
    }

    private void isLoading(boolean val){
        if (val){
            ((LinearLayout) findViewById(R.id.llLoading)).setVisibility(View.VISIBLE);
        }else {
            ((LinearLayout) findViewById(R.id.llLoading)).setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            ((Activity) context).finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void isEmpty(boolean val){
        if (val){
            ((TextView) findViewById(R.id.tvEmpty)).setVisibility(View.VISIBLE);
        }else {
            ((TextView) findViewById(R.id.tvEmpty)).setVisibility(View.GONE);
        }
    }

}