package uz.mq.handyway;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.camera2.params.RecommendedStreamConfigurationMap;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import uz.mq.handyway.Models.CartModel;
import uz.mq.handyway.Models.GoodDetalis;

public class ViewGoodActivity extends AppCompatActivity {
    Context context;
    HandyWayAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_good);
        setActionBar();
        context = this;
        api = new HandyWayAPI(Utils.getUserToken(context));
        initViews();
    }

    int minQuantity = 0;
    int maxQuantity = 0;
    int goodId = -1;
    int price = -1;
    TextView tvTitle, tvPrice, tvDesc, tvMethod, tvQuantity, tvInStock;
    ImageButton btnMinus, btnPlus;
    Button btnBuy;
    ImageView ivPic;
    private void initViews(){
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvDesc = (TextView) findViewById(R.id.tvDescription);
        tvInStock = (TextView) findViewById(R.id.tvInStock);
        tvMethod = (TextView) findViewById(R.id.tvPaymentMethod);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvQuantity = (TextView) findViewById(R.id.tvQuantity);
        btnMinus = (ImageButton) findViewById(R.id.btnMinus);
        btnPlus = (ImageButton) findViewById(R.id.btnPlus);
        ivPic = (ImageView) findViewById(R.id.ivPic);
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(tvQuantity.getText().toString()) - 1;
                if (quantity >= minQuantity){
                    tvQuantity.setText(quantity+"");
                }else {
                    Toast.makeText(context, context.getResources().getString(R.string.min_quanity)+": "+minQuantity, Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(tvQuantity.getText().toString()) + 1;
                if (quantity <= maxQuantity){
                    tvQuantity.setText(quantity+"");
                }else {
                    Toast.makeText(context, context.getResources().getString(R.string.max_quanity)+": "+maxQuantity, Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnBuy = (Button) findViewById(R.id.btnBuy);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (goodId >= 0){
                    addToCart(goodId, Integer.parseInt(tvQuantity.getText().toString()), price);
                }
            }
        });
        fillData();
    }

    private void addToCart(int id, int quantity, int price){
        CartUtils.addToCart(context, new CartModel(id, quantity, price));
        ((LinearLayout) findViewById(R.id.llSuccess)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.btnGoToCart)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, CartActivity.class));
                ((Activity) context).finish();
            }
        });
    }

    private void fillData(){
        isLoading(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final APIResponse response = api.getGood(getIntent().getIntExtra("id", 0));
                if (response.getCode() > 0){
                    switch (response.getCode()){
                        case 1:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    GoodDetalis goodDetalis = (GoodDetalis) response.getRes();
                                    tvDesc.setText(goodDetalis.getDescription());
                                    tvTitle.setText(goodDetalis.getName());
                                    tvMethod.setText(goodDetalis.getPayment_method().replace("[", "").replace("]", "").replace("'" ,""));
                                    if (goodDetalis.getMax_q() > 0){
                                        tvInStock.setText(goodDetalis.getMax_q()+"");
                                        tvQuantity.setText(goodDetalis.getMin_q()+"");
                                        btnMinus.setVisibility(View.VISIBLE);
                                        btnPlus.setVisibility(View.VISIBLE);
                                        btnBuy.setEnabled(true);
                                    }else {
                                        tvInStock.setText(R.string.not_available);
                                        tvQuantity.setText(R.string.not_available);
                                        btnMinus.setVisibility(View.GONE);
                                        btnPlus.setVisibility(View.GONE);
                                        btnBuy.setEnabled(false);
                                    }
                                    goodId = goodDetalis.getId();
                                    tvPrice.setText(Utils.convertPriceToString(goodDetalis.getPrice())+" "+getResources().getString(R.string.summ));
                                    minQuantity = goodDetalis.getMin_q();
                                    maxQuantity = goodDetalis.getMax_q();
                                    price = goodDetalis.getPrice();
                                    Picasso.get().load(HandyWayAPI.BASE_URL_MEDIA+goodDetalis.getPic_url()).error(R.drawable.no_image).into(ivPic, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            ivPic.setBackgroundColor(Color.parseColor("#ffffff"));
                                        }

                                        @Override
                                        public void onError(Exception e) {

                                        }
                                    });
                                    isLoading(false);
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
                        case 3:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.showErrorDialog(context, response.getMessage(), getLayoutInflater());
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
        }).start();
    }

    private void setActionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_icons8_left_4));
        setTitle(getIntent().getStringExtra("title"));
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());
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
}