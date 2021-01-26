package uz.mq.handyway;

import android.content.Context;
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

public class MainActivity extends AppCompatActivity {

    HandyWayAPI handyWayAPI;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        handyWayAPI = new HandyWayAPI(Utils.getUserToken(context));
        setActionBar();
        initViews();

    }

    DrawerLayout drawer;
    NavigationView navView;
    GridView categorysGird;
    CategorysGirdAdapter adapter;
    private void initViews(){
        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        categorysGird = (GridView) findViewById(R.id.categorysGird);


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



    private void setActionBar(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_modern_menu));
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
        }
        return true;
    }

}