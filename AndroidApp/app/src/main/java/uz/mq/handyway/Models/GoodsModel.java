package uz.mq.handyway.Models;

public class GoodsModel {
    int id;
    String title;
    int price;
    int min_quantity;
    int max_quantity;
    String pic_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMin_quantity() {
        return min_quantity;
    }

    public void setMin_quantity(int min_quantity) {
        this.min_quantity = min_quantity;
    }

    public int getMax_quantity() {
        return max_quantity;
    }

    public void setMax_quantity(int max_quantity) {
        this.max_quantity = max_quantity;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public GoodsModel(int id, String title, int price, int min_quantity, int max_quantity, String pic_url) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.min_quantity = min_quantity;
        this.max_quantity = max_quantity;
        this.pic_url = pic_url;
    }
}
