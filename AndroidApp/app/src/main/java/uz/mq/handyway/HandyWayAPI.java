package uz.mq.handyway;


import android.util.Log;
import android.view.textclassifier.ConversationActions;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HandyWayAPI {
    private static String BASE_URL = "https://handyway.uz/API/";
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
            return new APIResponse(json.getInt("code"), json.getString("message"), json.get("token"));
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


}
