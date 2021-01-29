package uz.mq.handyway.Models;

public class CartModel {
    int id;
    int quantity;
    int price;

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public CartModel(int id, int quantity, int price) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
    }
}
