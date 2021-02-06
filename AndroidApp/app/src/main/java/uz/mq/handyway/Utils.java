package uz.mq.handyway;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import uz.mq.handyway.Models.CartModel;

public class Utils {

    public static String EMPTY = "empty";

    public static void showErrorDialog(final Context context, final String error, LayoutInflater inflater){
        final BottomSheetDialog bottomSheerDialog = new BottomSheetDialog(context, R.style.SheetDialog);
        View parentView = inflater.inflate(R.layout.error_dialog ,null);

        ((TextView) parentView.findViewById(R.id.tvError)).setText(error);
        ((Button) parentView.findViewById(R.id.btnSendToDev)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToDev(context.toString()+"\n"+error);
                bottomSheerDialog.dismiss();
            }
        });
        bottomSheerDialog.setCancelable(true);
        bottomSheerDialog.setContentView(parentView);
        bottomSheerDialog.show();
    }


    public static void showSupportDialog(final Context context, LayoutInflater inflater){
        final BottomSheetDialog bottomSheerDialog = new BottomSheetDialog(context, R.style.SheetDialog);
        View parentView = inflater.inflate(R.layout.tech_support ,null);

        ((TextView) parentView.findViewById(R.id.tvTelegram)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSupportIntent(context);
            }
        });
        ((TextView) parentView.findViewById(R.id.tvInstagram)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://instagram.com/_u/handy_way_");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    ((Activity) context).startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    ((Activity) context).startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/handy_way_")));
                }
            }
        });
        ((TextView) parentView.findViewById(R.id.tvEmail)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, "support@handyway.uz");
                intent.putExtra(Intent.EXTRA_SUBJECT, "HandyWay APP");
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    ((Activity) context).startActivity(intent);
                }

            }
        });
        bottomSheerDialog.setCancelable(true);
        bottomSheerDialog.setContentView(parentView);
        bottomSheerDialog.show();
    }

    public static void showLanguageDialog(final Context context, LayoutInflater inflater){
        final BottomSheetDialog bottomSheerDialog = new BottomSheetDialog(context, R.style.SheetDialog);
        View parentView = inflater.inflate(R.layout.change_language ,null);

        ((TextView) parentView.findViewById(R.id.tvRUS)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resources res = context.getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                android.content.res.Configuration conf = res.getConfiguration();
                conf.setLocale(new Locale("RU".toLowerCase()));
                res.updateConfiguration(conf, dm);
                ((Activity) context).recreate();
            }
        });
        ((TextView) parentView.findViewById(R.id.tvUZB)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resources res = context.getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                android.content.res.Configuration conf = res.getConfiguration();
                conf.setLocale(new Locale("UZ".toLowerCase()));
                res.updateConfiguration(conf, dm);
                ((Activity) context).recreate();
            }
        });
        bottomSheerDialog.setCancelable(true);
        bottomSheerDialog.setContentView(parentView);
        bottomSheerDialog.show();
    }

    public static void sendToDev(String message){
        try {
            final String url = "https://api.telegram.org/bot1564780465:AAEwDdi0gCb8Lk-Tl_Hm1Xh58reKWQK5kFU/sendMessage?chat_id=365867849&text="+ URLEncoder.encode(message, "utf-8");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(url).build();
                    try {
                        client.newCall(request).execute();
                    }catch (Exception e){
                        Log.e("sendToDev", e.getLocalizedMessage());
                    }
                }
            }).start();
        }catch (Exception e){
            Log.e("sendToDev", e.getLocalizedMessage());
        }
    }

    public static int getTotalPrice(ArrayList<CartModel> models){
        int totalPrice = 0;
        for (int i = 0; i < models.size(); i++){
            totalPrice += models.get(i).getPrice() * models.get(i).getQuantity();
        }
        return totalPrice;
    }

    public static int getListColor(int id){
        String[] colors = {"f94144","f3722c","f8961e","f9844a","f9c74f","90be6d","43aa8b","4d908e","577590","277da1"};
        if (id < 10){
            return Color.parseColor("#"+colors[id]);
        }else{
            return getListColor(id-10);
        }
    }

    static boolean isLogin(Context ctx){
        SharedPreferences preferences = ctx.getSharedPreferences("User", Context.MODE_PRIVATE);
        return preferences.getBoolean("isLogin", false);
    }

    public static void setLogin(Context ctx, boolean val){
        SharedPreferences preferences = ctx.getSharedPreferences("User", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("isLogin", val).apply();
    }

    public static String getUserData(Context context, String name){
        SharedPreferences preferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return preferences.getString(name, EMPTY);
    }

    public static void setUserData(Context context, String name, String tel){
        SharedPreferences preferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        preferences.edit().putString("name", name).putString("tel", tel).apply();
    }

    public static void setUserToken(Context ctx, String token){
        SharedPreferences preferences = ctx.getSharedPreferences("User", Context.MODE_PRIVATE);
        preferences.edit().putString("token", token).apply();
    }

    public static String getUserToken(Context ctx){
        SharedPreferences preferences = ctx.getSharedPreferences("User", Context.MODE_PRIVATE);
        return preferences.getString("token", EMPTY);
    }

    public static void startSupportIntent(Context context){
        Intent tlgSupport = new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/handyway"));
        ((Activity) context).startActivity(tlgSupport);
    }

    public static boolean isOnline(Context ctx) {
        ConnectivityManager manager =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    public static void logOut(Context context){
        Utils.setLogin(context, false);
        ((Activity) context).startActivity(new Intent(context, LoginActivity.class));
        ((Activity) context).finish();
        ((Activity) context).finishAffinity();
    }

    public static String convertPriceToString(int price){
        String price_str = String.valueOf(price);
        Log.i("InputPrice:", price_str);
        String newprice = "";
        int counter = price_str.length()%3;
        for (int i=0; i < price_str.length(); i++){
            if (counter == 0){
                newprice += " ";
                counter = 3;
            }
            newprice += price_str.charAt(i);
            counter -= 1;
        }
        Log.i("OutputPrice:", newprice);
        return newprice;
    }

}
