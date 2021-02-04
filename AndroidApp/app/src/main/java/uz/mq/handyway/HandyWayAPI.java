package uz.mq.handyway;


import android.util.Log;
import android.view.textclassifier.ConversationActions;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import uz.mq.handyway.Models.CartModel;
import uz.mq.handyway.Models.CategoryModel;
import uz.mq.handyway.Models.GoodDetalis;
import uz.mq.handyway.Models.GoodsModel;
import uz.mq.handyway.Models.OrderModel;
import uz.mq.handyway.Models.ShopDetalis;

public class HandyWayAPI {
    public static String BASE_URL = "https://handyway.uz/API/";
    public static String URL = "https://handyway.uz/";
    public static String BASE_URL_MEDIA = "https://handyway.uz/media/";
    OkHttpClient client = new OkHttpClient();
    String token;

    public HandyWayAPI(String token) {
        this.token = token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public APIResponse logIn(String tel, String pass){
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("tel", tel)
                .addFormDataPart("password", pass)
                .addFormDataPart("device", android.os.Build.MANUFACTURER + android.os.Build.MODEL)
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL+"login.php")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String r_body = response.body().string();
            Log.i("ResponseBody", r_body);
            JSONObject json = new JSONObject(r_body);
            if (json.getInt("code") == 1){
                return new APIResponse(json.getInt("code"), json.getString("message"), json.getJSONObject("user_data"));
            }else {
                return new APIResponse(json.getInt("code"), json.getString("message"), null);
            }

        }catch (Exception e){
            return new APIResponse(0, e.getMessage(), null);
        }
    }


    public APIResponse checkIsActive(){
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", token)
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL+"check_is_active.php")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String r_body = response.body().string();
            Log.i("ResponseBody", r_body);
            JSONObject json = new JSONObject(r_body);
            return new APIResponse(json.getInt("code"), json.getString("message"), json.getBoolean("res"));
        }catch (Exception e){
            return new APIResponse(0, e.getMessage(), null);
        }
    }


    public APIResponse getCategorys(){
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", token)
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL+"get_categorys.php")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String r_body = response.body().string();
            Log.i("ResponseBody", r_body);
            JSONObject json = new JSONObject(r_body);
            JSONArray list = json.getJSONArray("res");
            ArrayList<CategoryModel> models = new ArrayList<>();
            for (int i=0; i<list.length(); i++){
                JSONObject item = list.getJSONObject(i);
                models.add(new CategoryModel(item.getInt("id"), item.getString("name")));
            }
            return new APIResponse(json.getInt("code"), json.getString("message"), models);
        }catch (Exception e){
            return new APIResponse(0, e.getMessage(), null);
        }
    }

    public APIResponse getGoods(int category_id){
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", token)
                .addFormDataPart("category_id", category_id+"")
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL+"get_goods.php")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String r_body = response.body().string();
            Log.i("ResponseBody", r_body);
            JSONObject json = new JSONObject(r_body);
            JSONArray list = json.getJSONArray("res");
            ArrayList<GoodsModel> models = new ArrayList<>();
            for (int i=0; i<list.length(); i++){
                JSONObject item = list.getJSONObject(i);
                models.add(new GoodsModel(item.getInt("id"), item.getString("name"), item.getInt("price"), item.getInt("min_quantity"), item.getInt("max_quantity"), item.getString("pic")));
            }
            return new APIResponse(json.getInt("code"), json.getString("message"), models);
        }catch (Exception e){
            return new APIResponse(0, e.getMessage(), null);
        }
    }

    public APIResponse getOrders(){
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", token)
                .addFormDataPart("approved", "false")
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL+"get_orders.php")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String r_body = response.body().string();
            Log.i("ResponseBody", r_body);
            JSONObject json = new JSONObject(r_body);
            JSONArray list = json.getJSONArray("res");
            ArrayList<OrderModel> models = new ArrayList<>();
            for (int i=0; i<list.length(); i++){
                JSONObject item = list.getJSONObject(i);

                Type typeOfObjectsList = new TypeToken<ArrayList<CartModel>>() {}.getType();
                ArrayList<CartModel> cartModels = new Gson().fromJson(item.getString("products"), typeOfObjectsList);
                models.add(new OrderModel(item.getInt("id"), item.getInt("status"), item.getString("date"), cartModels, item.getBoolean("isEditable")));
            }
            return new APIResponse(json.getInt("code"), json.getString("message"), models);
        }catch (Exception e){
            return new APIResponse(0, e.getMessage(), null);
        }
    }

    public APIResponse getApprovedOrders(){
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", token)
                .addFormDataPart("approved", "true")
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL+"get_orders.php")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String r_body = response.body().string();
            Log.i("ResponseBody", r_body);
            JSONObject json = new JSONObject(r_body);
            JSONArray list = json.getJSONArray("res");
            ArrayList<OrderModel> models = new ArrayList<>();
            for (int i=0; i<list.length(); i++){
                JSONObject item = list.getJSONObject(i);

                Type typeOfObjectsList = new TypeToken<ArrayList<CartModel>>() {}.getType();
                ArrayList<CartModel> cartModels = new Gson().fromJson(item.getString("products"), typeOfObjectsList);
                models.add(new OrderModel(item.getInt("id"), item.getInt("status"), item.getString("date"), cartModels, item.getBoolean("isEditable")));
            }
            return new APIResponse(json.getInt("code"), json.getString("message"), models);
        }catch (Exception e){
            return new APIResponse(0, e.getMessage(), null);
        }
    }

    public APIResponse search(String q, int category){
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", token)
                .addFormDataPart("category_id", category+"")
                .addFormDataPart("q", q)
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL+"search.php")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String r_body = response.body().string();
            Log.i("ResponseBody", r_body);
            JSONObject json = new JSONObject(r_body);
            JSONArray list = json.getJSONArray("res");
            ArrayList<GoodsModel> models = new ArrayList<>();
            for (int i=0; i<list.length(); i++){
                JSONObject item = list.getJSONObject(i);
                models.add(new GoodsModel(item.getInt("id"), item.getString("name"), item.getInt("price"), item.getInt("min_quantity"), item.getInt("max_quantity"), item.getString("pic")));
            }
            return new APIResponse(json.getInt("code"), json.getString("message"), models);
        }catch (Exception e){
            return new APIResponse(0, e.getMessage(), null);
        }
    }


    public APIResponse getGoodsByIds(int[] ids){
        Gson gson = new Gson();
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", token)
                .addFormDataPart("ids", gson.toJson(ids))
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL+"get_goods_by_ids.php")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String r_body = response.body().string();
            Log.i("ResponseBody", r_body);
            JSONObject json = new JSONObject(r_body);
            JSONArray list = json.getJSONArray("res");
            ArrayList<GoodsModel> models = new ArrayList<>();
            for (int i=0; i<list.length(); i++){
                JSONObject item = list.getJSONObject(i);
                models.add(new GoodsModel(item.getInt("id"), item.getString("name"), item.getInt("price"), item.getInt("min_quantity"), item.getInt("max_quantity"), item.getString("pic")));
            }
            return new APIResponse(json.getInt("code"), json.getString("message"), models);
        }catch (Exception e){
            return new APIResponse(0, e.getMessage(), null);
        }
    }

    public APIResponse getGood(int good_id){
        Gson gson = new Gson();
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", token)
                .addFormDataPart("good_id", good_id+"")
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL+"get_good.php")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String r_body = response.body().string();
            Log.i("ResponseBody", r_body);
            JSONObject json_ = new JSONObject(r_body);
            JSONObject json = json_.getJSONObject("res");
            if (json_.getInt("code") == 1){
                return new APIResponse(json_.getInt("code"), json_.getString("message"), new GoodDetalis(json.getInt("id"), json.getString("name"), json.getString("description"), json.getString("payment_method"), json.getInt("price"), json.getInt("min_quantity"), json.getInt("max_quantity"), json.getString("pic")));
            }else{
                return new APIResponse(json_.getInt("code"), json_.getString("message"), null);
            }
        }catch (Exception e){
            return new APIResponse(0, e.getMessage(), null);
        }
    }

    public APIResponse getShopDetalis(){
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", token)
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL+"get_shop_detalis.php")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String r_body = response.body().string();
            Log.i("ResponseBody", r_body);
            JSONObject json_ = new JSONObject(r_body);
            JSONObject json = json_.getJSONObject("res");
            if (json_.getInt("code") == 1){
                return new APIResponse(json_.getInt("code"), json_.getString("message"), new ShopDetalis(json.getInt("id"), json.getString("name"), json.getString("owner"), json.getString("inn"), json.getString("phone_number"), json.getString("district_str"), json.getString("landmark"), json.getString("category_str"), json.getString("photo"), json.getString("is_wholesaler").equals("1")));
            }else{
                return new APIResponse(json_.getInt("code"), json_.getString("message"), null);
            }

        }catch (Exception e){
            return new APIResponse(0, e.getMessage(), null);
        }
    }

    public APIResponse newOrder(String cart){
        Gson gson = new Gson();
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", token)
                .addFormDataPart("products", cart)
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL+"new_order.php")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String r_body = response.body().string();
            Log.i("ResponseBody", r_body);
            JSONObject json = new JSONObject(r_body);
            return new APIResponse(json.getInt("code"), json.getString("message"), json.getString("res"));
        }catch (Exception e){
            return new APIResponse(0, e.getMessage(), false);
        }
    }

    public APIResponse returnOrder(int orderId){
        Gson gson = new Gson();
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", token)
                .addFormDataPart("order_id", orderId+"")
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL+"return_order.php")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String r_body = response.body().string();
            Log.i("ResponseBody", r_body);
            JSONObject json = new JSONObject(r_body);
            return new APIResponse(json.getInt("code"), json.getString("message"), json.getString("res"));
        }catch (Exception e){
            return new APIResponse(0, e.getMessage(), false);
        }
    }

    public APIResponse changeOrder(int orderId, String cart){
        Gson gson = new Gson();
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", token)
                .addFormDataPart("order_id", orderId+"")
                .addFormDataPart("products", cart)
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL+"change_order.php")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String r_body = response.body().string();
            Log.i("ResponseBody", r_body);
            JSONObject json = new JSONObject(r_body);
            return new APIResponse(json.getInt("code"), json.getString("message"), json.getBoolean("res"));
        }catch (Exception e){
            return new APIResponse(0, e.getMessage(), false);
        }
    }

}

