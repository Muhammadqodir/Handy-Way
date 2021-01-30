package uz.mq.handyway;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import uz.mq.handyway.Adapters.CategoryAdapter;
import uz.mq.handyway.Adapters.CategorysGirdAdapter;
import uz.mq.handyway.Models.CategoryModel;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    HandyWayAPI handyWayAPI;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActionBar();
        context = this;
        handyWayAPI = new HandyWayAPI(Utils.getUserToken(context));
        initViews();
    }

    DrawerLayout drawer;
    NavigationView navView;
    GridView categorysGird;
    CategorysGirdAdapter adapter;
    private void initViews(){
        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        categorysGird = (GridView) findViewById(R.id.categorysGird);
        View navHeader = navView.getHeaderView(0);
        ((TextView) navHeader.findViewById(R.id.tvUserName)).setText(Utils.getUserData(context, "name"));
        ((TextView) navHeader.findViewById(R.id.tvUserTel)).setText(Utils.getUserData(context, "tel"));
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fillList();
    }

    private void fillList(){
        isLoading(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final APIResponse response = handyWayAPI.getCategorys();
                if (response.getCode() > 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (response.getCode()){
                                case 1:
                                    isLoading(false);
                                    ArrayList<CategoryModel> models = (ArrayList<CategoryModel>) response.getRes();
                                    if (models.size() > 0){
                                        isEmpty(false);
                                        adapter = new CategorysGirdAdapter(MainActivity.this, models);
                                        categorysGird.setAdapter(adapter);
                                    }else{
                                        isEmpty(true);
                                    }
                                    break;
                                case 2:
                                    Utils.logOut(context);
                                    break;
                            }
                        }
                    });
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_exit:
                new AlertDialog.Builder(context)
                        .setTitle(R.string.exit)
                        .setMessage(R.string.confirm_exit)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Utils.logOut(context);
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                break;
            case R.id.nav_profile:
                startActivity(new Intent(context, ProfileActivity.class));
                break;
            case R.id.nav_info:
                startActivity(new Intent(context, InfoActivity.class));
                break;
            case R.id.nav_cart:
                startActivity(new Intent(context, CartActivity.class));
                break;
            case R.id.nav_support:
                Utils.showSupportDialog(context, getLayoutInflater());
                break;
            case R.id.nav_orders:
                startActivity(new Intent(context, OrdersActivity.class));
                break;
            case R.id.nav_return:
                startActivity(new Intent(context, ReturnActivity.class));
                break;

        }
        drawer.close();
        return false;
    }

    private void setActionBar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_modern_menu));
        }catch (Exception e){

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            drawer.openDrawer(Gravity.LEFT);
        }else if(item.getItemId() == R.id.action_search){
            startActivity(new Intent(context, SearchActivity.class).putExtra("category_id", -1));
        }
        return true;
    }

}