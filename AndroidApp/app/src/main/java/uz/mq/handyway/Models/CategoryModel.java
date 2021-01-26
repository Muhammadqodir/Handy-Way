package uz.mq.handyway.Models;

import android.graphics.Color;

public class CategoryModel {
    int id;
    String title;

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

    public CategoryModel(int id, String title) {
        this.id = id;
        this.title = title;
    }
}
