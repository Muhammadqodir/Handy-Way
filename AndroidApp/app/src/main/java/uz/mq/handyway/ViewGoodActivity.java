package uz.mq.handyway;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import uz.mq.handyway.Models.CartModel;

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
    TextView tvTitle, tvPrice, tvDesc, tvMethod, tvQuantity, tvInStock;
    ImageButton btnMinus, btnPlus;
    Button btnBuy;
    private void initViews(){
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvDesc = (TextView) findViewById(R.id.tvDescription);
        tvInStock = (TextView) findViewById(R.id.tvInStock);
        tvMethod = (TextView) findViewById(R.id.tvPaymentMethod);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvQuantity = (TextView) findViewById(R.id.tvQuantity);
        btnMinus = (ImageButton) findViewById(R.id.btnMinus);
        btnPlus = (ImageButton) findViewById(R.id.btnPlus);
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
                    addToCart(goodId, Integer.parseInt(tvQuantity.getText().toString()));
                }
            }
        });
    }

    private void addToCart(int id, int quantity){
        CartUtils.addToCart(context, new CartModel(id, quantity));
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

}