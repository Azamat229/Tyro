package com.student.tyro.Model;

public class Reportcardsublist {

    String id,catid,reportname,status,crated,rating;

    public Reportcardsublist(String id, String catid, String reportname, String status, String crated,String ratng) {
        this.id = id;
        this.catid = catid;
        this.reportname = reportname;
        this.status = status;
        this.crated = crated;
        this.rating=ratng;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getReportname() {
        return reportname;
    }

    public void setReportname(String reportname) {
        this.reportname = reportname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCrated() {
        return crated;
    }

    public void setCrated(String crated) {
        this.crated = crated;
    }
}
