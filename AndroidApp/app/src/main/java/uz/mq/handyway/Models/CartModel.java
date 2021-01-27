package uz.mq.handyway.Models;

public class CartModel {
    int id;
    int quantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public CartModel(int id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }
}
