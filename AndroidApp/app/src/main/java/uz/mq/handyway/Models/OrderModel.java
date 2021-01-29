package uz.mq.handyway.Models;

public class OrderModel {
    int id, status;
    String date;
    boolean isEditable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public OrderModel(int id, int status, String date, boolean isEditable) {
        this.id = id;
        this.status = status;
        this.date = date;
        this.isEditable = isEditable;
    }
}
