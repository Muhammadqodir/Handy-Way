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
import android.graphics.RenderNode;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import okhttp3.internal.Util;
import uz.mq.handyway.Adapters.CartAdapter;
import uz.mq.handyway.Models.GoodsModel;

public class CartActivity extends AppCompatActivity {
    Context context;
    HandyWayAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        context = this;
        api = new HandyWayAPI(Utils.getUserToken(context));
        setActionBar();
        initViews();
    }

    RecyclerView recyclerView;
    CartAdapter adapter;
    private void initViews(){
        recyclerView = (RecyclerView) findViewById(R.id.cartList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ((LinearLayout) findViewById(R.id.send_order)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle(R.string.arrange_order)
                        .setMessage(R.string.confirm_arrange_order)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ((ProgressBar) findViewById(R.id.progress)).setVisibility(View.VISIBLE);
                                ((TextView) findViewById(R.id.tvBtnTitle)).setVisibility(View.GONE);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final APIResponse response = api.newOrder(CartUtils.getCartJsonString(context));
                                        if (response.getCode() > 0){
                                            switch (response.getCode()){
                                                case 1:
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            ((ProgressBar) findViewById(R.id.progress)).setVisibility(View.GONE);
                                                            ((TextView) findViewById(R.id.tvTitle)).setVisibility(View.VISIBLE);
                                                            getSupportActionBar().hide();
                                                            CartUtils.clearCart(context);
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
                                                    break;
                                                case 2:
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            ((ProgressBar) findViewById(R.id.progress)).setVisibility(View.GONE);
                                                            ((TextView) findViewById(R.id.tvTitle)).setVisibility(View.VISIBLE);
                                                            Utils.logOut(context);
                                                        }
                                                    });
                                                    break;
                                            }
                                        }else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ((ProgressBar) findViewById(R.id.progress)).setVisibility(View.GONE);
                                                    ((TextView) findViewById(R.id.tvTitle)).setVisibility(View.VISIBLE);
                                                    Utils.showErrorDialog(context, response.getMessage(), getLayoutInflater());
                                                }
                                            });
                                        }
                                    }
                                }).start();
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });
        fillList();

    }

    private void fillList(){
        isLoading(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final APIResponse response = api.getGoodsByIds(CartUtils.getGoodsIds(context));
                if (response.getCode() > 0){
                    switch (response.getCode()){
                        case 1:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isLoading(false);
                                    ArrayList<GoodsModel> goodsModels = (ArrayList<GoodsModel>) response.getRes();
                                    if (goodsModels.size() > 0){
                                        isEmpty(false);
                                        ((TextView) findViewById(R.id.tvTotalPrice)).setText(getResources().getString(R.string.total) + " " + Utils.convertPriceToString(Utils.getTotalPrice(CartUtils.getCart(context)))+" "+getResources().getString(R.string.summ));
                                        adapter = new CartAdapter(context, CartUtils.getCart(context), goodsModels, new Runnable() {
                                            @Override
                                            public void run() {
                                                isEmpty(true);
                                            }
                                        }, new Runnable() {
                                            @Override
                                            public void run() {
                                                ((TextView) findViewById(R.id.tvTotalPrice)).setText(getResources().getString(R.string.total) + " " + Utils.convertPriceToString(Utils.getTotalPrice(CartUtils.getCart(context)))+" "+getResources().getString(R.string.summ));
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
        setTitle(getResources().getString(R.string.cart));
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
        }else if(item.getItemId() == R.id.action_clear_cart){
            if (adapter != null){
                new AlertDialog.Builder(context)
                        .setTitle(R.string.clear_cart)
                        .setMessage(R.string.confirm_action)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                CartUtils.clearCart(context);
                                adapter.clearCart();
                                isEmpty(true);
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }else {
                Toast.makeText(context, R.string.cart_is_empty, Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void isEmpty(boolean val){
        if (val){
            ((TextView) findViewById(R.id.tvEmpty)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.llBottomBar)).setVisibility(View.GONE);
        }else {
            ((TextView) findViewById(R.id.tvEmpty)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.llBottomBar)).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return true;
    }
}