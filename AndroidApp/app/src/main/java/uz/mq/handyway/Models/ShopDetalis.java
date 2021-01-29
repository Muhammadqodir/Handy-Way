package uz.mq.handyway.Models;

public class ShopDetalis {
    int id;
    String name, owner, inn, phone_num, district, landmark, category, photo;
    boolean isWholesaler;

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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isWholesaler() {
        return isWholesaler;
    }

    public void setIsWholesaler(boolean idWholesaler) {
        this.isWholesaler = idWholesaler;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public ShopDetalis(int id, String name, String owner, String inn, String phone_num, String district, String landmark, String category, String photo, boolean idWholesaler) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.inn = inn;
        this.phone_num = phone_num;
        this.district = district;
        this.landmark = landmark;
        this.category = category;
        this.photo = photo;
        this.isWholesaler = idWholesaler;
    }
}
