package uz.mq.handyway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import uz.mq.handyway.Adapters.OrderAdapter;
import uz.mq.handyway.Adapters.OrderDetalisAdapter;
import uz.mq.handyway.Models.CartModel;
import uz.mq.handyway.Models.GoodsModel;
import uz.mq.handyway.Models.OrderModel;

public class OrderDetalisActivity extends AppCompatActivity {
    Context context;
    HandyWayAPI api;
    OrderModel orderModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detalis);
        context = this;
        orderModel = new Gson().fromJson(getIntent().getStringExtra("data"), OrderModel.class);
        api = new HandyWayAPI(Utils.getUserToken(context));
        setActionBar();
        initViews();
    }

    RecyclerView recyclerView;
    OrderDetalisAdapter adapter;
    private void initViews(){
        recyclerView = (RecyclerView) findViewById(R.id.orderDetalisList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (orderModel.isEditable()){
            ((LinearLayout) findViewById(R.id.llActions)).setVisibility(View.VISIBLE);
            ((Button) findViewById(R.id.btnSave)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(context)
                            .setTitle(R.string.save_changes)
                            .setMessage(R.string.confirm_action)
                            .setNegativeButton(R.string.cancel, null)
                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            changeOrder();
                                        }
                                    }).start();
                                }
                            }).show();
                }
            });
            ((ImageButton) findViewById(R.id.btnCancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(context)
                            .setTitle(R.string.cancel_order)
                            .setMessage(R.string.confirm_action)
                            .setNegativeButton(R.string.cancel, null)
                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    adapter.removeAllItems();
                                    isEmpty(true);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            changeOrder();
                                        }
                                    }).start();
                                }
                            }).show();
                }
            });
        }else {
            ((LinearLayout) findViewById(R.id.llActions)).setVisibility(View.GONE);
        }
        fillList();
    }

    private void changeOrder(){
        String items = "[]";
        if (adapter.getItemCount() > 0){
            items = new Gson().toJson(adapter.getCartItems());
        }
        final APIResponse response = api.changeOrder(orderModel.getId(), items);
        if (response.getCode() > 0){
            switch (response.getCode()){
                case 1:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, R.string.done, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(context, MainActivity.class));
                            ((Activity) context).finishAffinity();
                        }
                    });
                    break;
                case 2:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.logOut(context);
                        }
                    });
                    break;
            }
        }else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Utils.showErrorDialog(context, response.getMessage(), getLayoutInflater());
                }
            });
        }
    }

    private void fillList(){
        isLoading(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int[] ids = new int[orderModel.getCartItems().size()];
                for (int i = 0; i<ids.length; i++){
                    ids[i] = orderModel.getCartItems().get(i).getId();
                }
                final APIResponse response = api.getGoodsByIds(ids);
                if (response.getCode() > 0){
                    switch (response.getCode()){
                        case 1:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isLoading(false);
                                    ArrayList<GoodsModel> goodsModels = (ArrayList<GoodsModel>) response.getRes();
                                    ((TextView) findViewById(R.id.tvTotalPrice)).setText(getResources().getString(R.string.total) + " " + Utils.convertPriceToString(Utils.getTotalPrice(orderModel.getCartItems()))+" "+getResources().getString(R.string.summ));
                                    adapter = new OrderDetalisAdapter(context, orderModel.getCartItems(), goodsModels, orderModel.isEditable(), new Runnable() {
                                        @Override
                                        public void run() {
                                            isEmpty(true);
                                        }
                                    }, new Runnable() {
                                        @Override
                                        public void run() {
                                            ((TextView) findViewById(R.id.tvTotalPrice)).setText(getResources().getString(R.string.total) + " " + Utils.convertPriceToString(Utils.getTotalPrice(adapter.getCartItems()))+" "+getResources().getString(R.string.summ));
                                        }
                                    });
                                    recyclerView.setAdapter(adapter);
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
        setTitle(context.getResources().getString(R.string.order)+" №"+orderModel.getId());
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