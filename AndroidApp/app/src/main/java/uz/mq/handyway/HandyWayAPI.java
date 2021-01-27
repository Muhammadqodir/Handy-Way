package uz.mq.handyway;


import android.util.Log;
import android.view.textclassifier.ConversationActions;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import uz.mq.handyway.Models.CategoryModel;
import uz.mq.handyway.Models.GoodsModel;

public class HandyWayAPI {
    public static String BASE_URL = "https://handyway.uz/API/";
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
            return new APIResponse(json.getInt("code"), json.getString("message"), json.getJSONObject("user_data"));
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


}
