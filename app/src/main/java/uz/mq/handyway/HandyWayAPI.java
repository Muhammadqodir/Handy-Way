package uz.mq.handyway;


import android.view.textclassifier.ConversationActions;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HandyWayAPI {
    private static String BASE_URL = "https://handyway.uz/api";
    OkHttpClient client = new OkHttpClient();
    String token;

    public HandyWayAPI(String token) {
        this.token = token;
    }

    public APIResponse getCategorys(){
        return getResponse(new FormBody.Builder()
                .add("action", "getCategorys")
                .add("token", token)
                .build());
    }

    public APIResponse getResponse(RequestBody formBody){
        Request request = new Request.Builder()
                .url(BASE_URL + "/login.php")
                .post(formBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200){
                return new APIResponse(true, response.body().string(), "");
            }else{
                return new APIResponse(false, null, "Rsponse Code:" + response.code());
            }
        }catch (Exception e){
            return new APIResponse(false, null, e.getMessage());
        }
    }

    public static APIResponse logIN(String inn, String pass){
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("inn", inn)
                .add("pass", pass)
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL + "/login.php")
                .post(formBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200){
                return new APIResponse(true, response.body().string(), "");
            }else{
                return new APIResponse(false, null, "Rsponse Code:" + response.code());
            }
        }catch (Exception e){
            return new APIResponse(false, null, e.getMessage());
        }

    }
}
