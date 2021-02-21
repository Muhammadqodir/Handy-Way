package uz.mq.handyway.Models;

import org.json.JSONArray;

public class BrandModel {
    int id;
    String title;
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

    public BrandModel(int id, String title, JSONArray categories) {
        this.id = id;
        this.title = title;
        this.categories = categories;
    }
}
