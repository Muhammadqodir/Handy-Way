package uz.mq.handyway.Models;

import java.util.ArrayList;

public class OrderModel {
    int id, status;
    String date;
    ArrayList<CartModel> cartItems;
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

    public ArrayList<CartModel> getCartItems() {
        return cartItems;
    }

    public void setCartItems(ArrayList<CartModel> cartItems) {
        this.cartItems = cartItems;
    }

    public OrderModel(int id, int status, String date, ArrayList<CartModel> cartItems, boolean isEditable) {
        this.id = id;
        this.status = status;
        this.date = date;
        this.cartItems = cartItems;
        this.isEditable = isEditable;
    }
}
