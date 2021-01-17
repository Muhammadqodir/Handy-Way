package uz.mq.handyway;

public class APIResponse {
    boolean isSuccess;
    Object data;
    String error;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public APIResponse(boolean isSuccess, Object data, String error) {
        this.isSuccess = isSuccess;
        this.data = data;
        this.error = error;
    }
}
