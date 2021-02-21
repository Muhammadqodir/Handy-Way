package uz.mq.handyway.Models;

import org.json.JSONArray;

public class BrandModel {
    int id;
    String title;
    String logo;
    JSONArray categories;

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

    public JSONArray getCategories() {
        return categories;
    }

    public void setCategories(JSONArray categories) {
        this.categories = categories;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public BrandModel(int id, String title, String logo, JSONArray categories) {
        this.id = id;
        this.title = title;
        this.logo = logo;
        this.categories = categories;
    }
}
