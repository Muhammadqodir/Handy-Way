package uz.mq.handyway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EdgeEffect;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import uz.mq.handyway.Adapters.GoodsGirdAdapter;
import uz.mq.handyway.Models.GoodsModel;

public class SearchActivity extends AppCompatActivity {

    Context context;
    HandyWayAPI api;
    int category_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        context = this;
        api = new HandyWayAPI(Utils.getUserToken(context));
        category_id = getIntent().getIntExtra("category_id", -1);
        setActionBar();
        initViews();
    }

    @Override
    protected void onStart() {
        tvCart.setText(CartUtils.getCartQuantity(context)+"");
        super.onStart();
    }

    GridView girdView;
    GoodsGirdAdapter adapter;
    View cartParent;
    TextView tvCart;
    FloatingActionButton fab;
    SearchView searchView;
    private void initViews(){
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() >= 3){
                    search(s);
                }else {
                    Toast.makeText(context, R.string.at_least, Toast.LENGTH_SHORT).show();
                    searchView.setFocusable(true);
                    searchView.setIconified(false);
                    searchView.requestFocusFromTouch();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        girdView = (GridView) findViewById(R.id.goodsGird);
        cartParent = findViewById(R.id.cartParent);
        tvCart = (TextView) findViewById(R.id.tvCart);
        tvCart.setText(CartUtils.getCartQuantity(context)+"");
        fab = (FloatingActionButton) findViewById(R.id.fabCart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, CartActivity.class));
            }
        });

    }

    private void search(final String q){
        isEmpty(false);
        isLoading(true);
        girdView.setAdapter(null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final APIResponse response = api.search(q, category_id);
                if (response.getCode() > 0){
                    switch (response.getCode()){
                        case 1:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isLoading(false);
                                    ArrayList<GoodsModel> models = (ArrayList<GoodsModel>) response.getRes();
                                    if (models.size() > 0){
                                        isEmpty(false);
                                        adapter = new GoodsGirdAdapter(context, models, cartParent, tvCart);
                                        girdView.setAdapter(adapter);
                                    }else{
                                        isEmpty(true);
                                    }
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
                            isLoading(false);
                            isEmpty(true);
                            Utils.showErrorDialog(context, response.getMessage(), getLayoutInflater());
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            ((Activity) context).finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void isLoading(boolean val){
        if (val){
            ((LinearLayout) findViewById(R.id.llLoading)).setVisibility(View.VISIBLE);
        }else {
            ((LinearLayout) findViewById(R.id.llLoading)).setVisibility(View.GONE);
        }
    }

    private void isEmpty(boolean val){
        if (val){
            ((TextView) findViewById(R.id.tvEmpty)).setVisibility(View.VISIBLE);
        }else {
            ((TextView) findViewById(R.id.tvEmpty)).setVisibility(View.GONE);
        }
    }


    private void setActionBar(){
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }

}