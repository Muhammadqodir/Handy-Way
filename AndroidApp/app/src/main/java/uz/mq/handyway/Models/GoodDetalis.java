package uz.mq.handyway.Models;

public class GoodDetalis {
    int id;
    String name, description, payment_method;
    int price;
    int min_q, max_q;
    String pic_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMin_q() {
        return min_q;
    }

    public void setMin_q(int min_q) {
        this.min_q = min_q;
    }

    public int getMax_q() {
        return max_q;
    }

    public void setMax_q(int max_q) {
        this.max_q = max_q;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public GoodDetalis(int id, String name, String description, String payment_method, int price, int min_q, int max_q, String pic_url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.payment_method = payment_method;
        this.price = price;
        this.min_q = min_q;
        this.max_q = max_q;
        this.pic_url = pic_url;
    }
}
