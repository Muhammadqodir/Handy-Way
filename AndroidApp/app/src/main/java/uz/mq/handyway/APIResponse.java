package uz.mq.handyway;

public class APIResponse {
    int code;
    String message;
    Object res;

    public APIResponse(int code, String message, Object res) {
        this.code = code;
        this.message = message;
        this.res = res;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRes() {
        return res;
    }

    public void setRes(Object res) {
        this.res = res;
    }
}
