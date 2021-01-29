package uz.mq.handyway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.jar.JarEntry;

import uz.mq.handyway.Models.ShopDetalis;

public class ProfileActivity extends AppCompatActivity {
    Context context;
    HandyWayAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = this;
        api = new HandyWayAPI(Utils.getUserToken(context));
        setActionBar();
        initViews();
    }

    private void initViews(){
        isLoading(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final APIResponse response = api.getShopDetalis();
                if (response.getCode() > 0){
                    switch (response.getCode()){
                        case 1:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isLoading(false);
                                    ShopDetalis detalis = (ShopDetalis) response.getRes();
                                    ((TextView) findViewById(R.id.tvOrgName)).setText(detalis.getName()+" ("+detalis.getCategory()+")");
                                    ((TextView) findViewById(R.id.tvOwnerName)).setText(detalis.getOwner()+"");
                                    ((TextView) findViewById(R.id.tvPhoneNum)).setText(detalis.getPhone_num()+"");
                                    ((TextView) findViewById(R.id.tvINN)).setText(detalis.getInn()+"");
                                    ((TextView) findViewById(R.id.tvAddress)).setText(detalis.getDistrict()+"");
                                    ((TextView) findViewById(R.id.tvLandmark)).setText(detalis.getLandmark()+"");
                                    int whichSaler = R.string.no;
                                    if (detalis.isWholesaler()){
                                        whichSaler = R.string.yes;
                                    }
                                    ((TextView) findViewById(R.id.isWholesaler)).setText(whichSaler);
                                    Picasso.get().load(HandyWayAPI.BASE_URL_MEDIA+detalis.getPhoto()).error(R.drawable.no_image).into(((ImageView) findViewById(R.id.ivPic)));
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
        }).start();
    }

    private void setActionBar(){
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){

        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_icons8_left_3));
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