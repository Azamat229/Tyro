package com.student.tyro.Model;

public class Instructor {

    String instruct_language,instruct_id,instruct_firstname,instruct_img,instruct_rate,instruct_distance;

    public Instructor(String instruct_language, String instruct_id, String instruct_firstname, String instruct_img, String instruct_rate, String instruct_distance) {

        this.instruct_language = instruct_language;
        this.instruct_id = instruct_id;
        this.instruct_firstname = instruct_firstname;
        this.instruct_img = instruct_img;
        this.instruct_rate = instruct_rate;
        this.instruct_distance = instruct_distance;
    }

    public String getInstruct_rate() {
        return instruct_rate;
    }

    public void setInstruct_rate(String instruct_rate) {
        this.instruct_rate = instruct_rate;
    }

    public String getInstruct_language() {
        return instruct_language;
    }

    public void setInstruct_language(String instruct_language) {
        this.instruct_language = instruct_language;
    }

    public String getInstruct_id() {
        return instruct_id;
    }

    public void setInstruct_id(String instruct_id) {
        this.instruct_id = instruct_id;
    }

    public String getInstruct_firstname() {
        return instruct_firstname;
    }

    public void setInstruct_firstname(String instruct_firstname) {
        this.instruct_firstname = instruct_firstname;
    }

    public String getInstruct_img() {
        return instruct_img;
    }

    public void setInstruct_img(String instruct_img) {
        this.instruct_img = instruct_img;
    }

    public String getInstruct_distance() {
        return instruct_distance;
    }

    public void setInstruct_distance(String instruct_distance) {
        this.instruct_distance = instruct_distance;
    }
}
