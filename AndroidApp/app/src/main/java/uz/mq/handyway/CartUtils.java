package uz.mq.handyway;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import uz.mq.handyway.Models.CartModel;

public class CartUtils {
    public static void addToCart(Context ctx, CartModel model){
        ArrayList<CartModel> cartModels;
        Gson gson = new Gson();
        SharedPreferences sharedPreference = ctx.getSharedPreferences("Cart", Context.MODE_PRIVATE);
        String cash_str = sharedPreference.getString("History", "empty");
        if (cash_str.equals("empty")){
            cartModels = new ArrayList<>();
            cartModels.add(model);
        }else{
            Type typeOfObjectsList = new TypeToken<ArrayList<CartModel>>() {}.getType();
            cartModels = gson.fromJson(cash_str, typeOfObjectsList);
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
}
