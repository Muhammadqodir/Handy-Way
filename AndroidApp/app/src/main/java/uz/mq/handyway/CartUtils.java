package uz.mq.handyway;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import uz.mq.handyway.Models.CartModel;

public class CartUtils {
    public static void addToCart(Context ctx, CartModel model){
        ArrayList<CartModel> cartModels = new ArrayList<>();
        Gson gson = new Gson();
        SharedPreferences sharedPreference = ctx.getSharedPreferences("Cart", Context.MODE_PRIVATE);
        String cash_str = sharedPreference.getString("Cart", "empty");
        if (!cash_str.equals("empty")){
            Type typeOfObjectsList = new TypeToken<ArrayList<CartModel>>() {}.getType();
            cartModels = gson.fromJson(cash_str, typeOfObjectsList);
        }
        boolean isExist = false;
        for (int i=0; i<cartModels.size(); i++){
            if (cartModels.get(i).getId() == model.getId()){
                cartModels.get(i).setQuantity(cartModels.get(i).getQuantity()+model.getQuantity());
                isExist = true;
                break;
            }
        }
        if (!isExist){
            cartModels.add(model);
        }
        sharedPreference.edit().putString("Cart", gson.toJson(cartModels)).apply();
    }

    public static void removeItem(Context ctx, int id){
        ArrayList<CartModel> cartModels;
        Gson gson = new Gson();
        SharedPreferences sharedPreference = ctx.getSharedPreferences("Cart", Context.MODE_PRIVATE);
        String cash_str = sharedPreference.getString("Cart", "empty");
        Type typeOfObjectsList = new TypeToken<ArrayList<CartModel>>() {}.getType();
        cartModels = gson.fromJson(cash_str, typeOfObjectsList);
        for (int i=0; i<cartModels.size(); i++){
            if (cartModels.get(i).getId() == id){
                cartModels.remove(i);
                break;
            }
        }
        sharedPreference.edit().putString("Cart", gson.toJson(cartModels)).apply();
    }

    public static void changeItemQuanity(Context ctx, int id, int quanity){
        ArrayList<CartModel> cartModels;
        Gson gson = new Gson();
        SharedPreferences sharedPreference = ctx.getSharedPreferences("Cart", Context.MODE_PRIVATE);
        String cash_str = sharedPreference.getString("Cart", "empty");
        Type typeOfObjectsList = new TypeToken<ArrayList<CartModel>>() {}.getType();
        cartModels = gson.fromJson(cash_str, typeOfObjectsList);
        for (int i=0; i<cartModels.size(); i++){
            if (cartModels.get(i).getId() == id){
                cartModels.get(i).setQuantity(quanity);
                break;
            }
        }
        sharedPreference.edit().putString("Cart", gson.toJson(cartModels)).apply();
    }

    public static ArrayList<CartModel> getCart(Context ctx){
        ArrayList<CartModel> cartModels = new ArrayList<>();
        Gson gson = new Gson();
        SharedPreferences sharedPreference = ctx.getSharedPreferences("Cart", Context.MODE_PRIVATE);
        String cash_str = sharedPreference.getString("Cart", "empty");
        if (!cash_str.equals("empty")){
            Type typeOfObjectsList = new TypeToken<ArrayList<CartModel>>() {}.getType();
            cartModels = gson.fromJson(cash_str, typeOfObjectsList);
        }
        return cartModels;
    }

    public static int getCartQuantity(Context ctx){
        ArrayList<CartModel> cartModels = new ArrayList<>();
        Gson gson = new Gson();
        SharedPreferences sharedPreference = ctx.getSharedPreferences("Cart", Context.MODE_PRIVATE);
        String cash_str = sharedPreference.getString("Cart", "empty");
        if (!cash_str.equals("empty")){
            Type typeOfObjectsList = new TypeToken<ArrayList<CartModel>>() {}.getType();
            cartModels = gson.fromJson(cash_str, typeOfObjectsList);
        }
        return cartModels.size();
    }

    public static String getCartJsonString(Context ctx){
        ArrayList<CartModel> cartModels = new ArrayList<>();
        Gson gson = new Gson();
        SharedPreferences sharedPreference = ctx.getSharedPreferences("Cart", Context.MODE_PRIVATE);
        return sharedPreference.getString("Cart", "empty");
    }

    public static int[] getGoodsIds(Context ctx){
        ArrayList<CartModel> cartModels = new ArrayList<>();
        Gson gson = new Gson();
        SharedPreferences sharedPreference = ctx.getSharedPreferences("Cart", Context.MODE_PRIVATE);
        String cash_str = sharedPreference.getString("Cart", "empty");
        if (!cash_str.equals("empty")){
            Type typeOfObjectsList = new TypeToken<ArrayList<CartModel>>() {}.getType();
            cartModels = gson.fromJson(cash_str, typeOfObjectsList);
        }
        int[] ids = new int[cartModels.size()];
        for (int i = 0; i < cartModels.size(); i++){
            ids[i] = cartModels.get(i).getId();
        }
        return ids;
    }

    public static void clearCart(Context ctx){
        ArrayList<CartModel> cartModels = new ArrayList<>();
        Gson gson = new Gson();
        SharedPreferences sharedPreference = ctx.getSharedPreferences("Cart", Context.MODE_PRIVATE);
        sharedPreference.edit().putString("Cart", gson.toJson(cartModels)).apply();
    }


}
