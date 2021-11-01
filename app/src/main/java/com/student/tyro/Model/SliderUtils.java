package com.student.tyro.Model;

public class SliderUtils {

    String id,descript,image,status,created,imagepath;

    public SliderUtils(String id, String descript, String image, String status, String created,String imagepath) {
        this.id = id;
        this.descript = descript;
        this.image = image;
        this.status = status;
        this.created = created;
        this.imagepath = imagepath;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
