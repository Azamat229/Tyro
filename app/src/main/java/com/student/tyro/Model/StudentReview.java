package com.student.tyro.Model;

public class StudentReview {

    String base_path, picture, rating, review, student_name, time;

    public StudentReview(String base_path, String picture, String rating, String review, String student_name, String time) {

        this.base_path = base_path;
        this.picture = picture;
        this.rating = rating;
        this.review = review;
        this.student_name = student_name;
        this.time = time;
    }

    public String getBase_path() {
        return base_path;
    }

    public void setBase_path(String base_path) {
        this.base_path = base_path;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
