package com.student.tyro.Model;

public class Faqcatgories {
    String id,catgory,status,created_on,catid,title,description;



    public Faqcatgories(String id,String catid,String title,String description,String status,String created_on) {
        this.id = id;
        this.status = status;
        this.created_on = created_on;
        this.catid = catid;
        this.title = title;
        this.description = description;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }
}
