package com.student.tyro.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class StaySafeModel implements Serializable {
    String title, subtitle;
    int imageView;

    public StaySafeModel(int imageView, String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
        this.imageView = imageView;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getImageView() {
        return imageView;
    }
}
