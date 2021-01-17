package uz.mq.handyway;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

    public static String EMPTY = "empty";

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

    static void setLogin(Context ctx, boolean val){
        SharedPreferences preferences = ctx.getSharedPreferences("User", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("isLogin", val).apply();
    }

    static void setUserToken(Context ctx, String token){
        SharedPreferences preferences = ctx.getSharedPreferences("User", Context.MODE_PRIVATE);
        preferences.edit().putString("token", token).apply();
    }

    static String getUserData(Context ctx){
        SharedPreferences preferences = ctx.getSharedPreferences("User", Context.MODE_PRIVATE);
        return preferences.getString("token", EMPTY);
    }

    public static boolean isOnline(Context ctx) {
        ConnectivityManager manager =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }

}
