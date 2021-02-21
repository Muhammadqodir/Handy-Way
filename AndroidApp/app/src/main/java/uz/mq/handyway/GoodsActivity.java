package uz.mq.handyway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import kotlin.random.AbstractPlatformRandom;
import uz.mq.handyway.Adapters.GoodsGirdAdapter;
import uz.mq.handyway.Models.GoodsModel;

public class GoodsActivity extends AppCompatActivity {
    Context context;
    HandyWayAPI api;
    int category_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        context = this;
        category_id = getIntent().getIntExtra("category_id", 0);
        api = new HandyWayAPI(Utils.getUserToken(context));
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
    private void initViews(){
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
        goToStep(0, 0);
    }

    private void setActionBar(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_icons8_left_4));
        setTitle(getIntent().getStringExtra("title"));
    }

    private void getGoods(final int brandId){
        isLoading(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final APIResponse response = api.getGoodsByBrand(brandId);
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
    
    int curStep = 0;
    int selBrand = 0;
    public void goToStep(int step, int braidId){
        switch (step){
            case -1:
                ((Activity) context).finish();
                break;
            case 0:

                break;
            case 1:
                getGoods(braidId);
                break;
        }
        curStep = step;
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
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            goToStep(curStep-1);
        }else if (item.getItemId() == R.id.action_search){
            startActivity(new Intent(context, SearchActivity.class).putExtra("category_id", category_id));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        goToStep(curStep-1);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}