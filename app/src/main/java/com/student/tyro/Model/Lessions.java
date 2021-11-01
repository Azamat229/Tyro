package com.student.tyro.Model;

public class Lessions {
    String id,sid,instid,location,strt_tme,end_tme,bookdate,bookhrs,username,user_image,
            sts,cancelreason,prce,uniquid,paytype,status,update_latitude,update_longitude;

    public String getStatus() {
        return status;
    }

    public String getUpdate_latitude() {
        return update_latitude;
    }

    public String getUpdate_longitude() {
        return update_longitude;
    }

    public Lessions(String id, String sid, String instid, String strt_tme, String end_tme, String bookdate,
                    String bookhrs, String username, String user_image, String location, String price,
                    String uniid, String sts, String paymenttype, String cancelreason,
                    String status1,String update_latitude,String update_longitude) {

        this.id = id;
        this.sid = sid;
        this.instid = instid;
        this.strt_tme = strt_tme;
        this.end_tme = end_tme;
        this.bookdate = bookdate;
        this.bookhrs = bookhrs;
        //this.created = created;
        this.username = username;
        this.user_image = user_image;
        this.location = location;
        this.sts = sts;
        this.cancelreason = cancelreason;
        this.prce = price;
        this.uniquid = uniid;
        this.paytype = paymenttype;
        this.status = status1;
        this.update_latitude = update_latitude;
        this.update_longitude = update_longitude;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getPrce() {
        return prce;
    }

    public void setPrce(String prce) {
        this.prce = prce;
    }

    public String getUniquid() {
        return uniquid;
    }

    public void setUniquid(String uniquid) {
        this.uniquid = uniquid;
    }

    public String getStrt_tme() {
        return strt_tme;
    }

    public void setStrt_tme(String strt_tme) {
        this.strt_tme = strt_tme;
    }

    public String getEnd_tme() {
        return end_tme;
    }

    public void setEnd_tme(String end_tme) {
        this.end_tme = end_tme;
    }

    public String getCancelreason() {
        return cancelreason;
    }

    public void setCancelreason(String cancelreason) {
        this.cancelreason = cancelreason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getInstid() {
        return instid;
    }

    public void setInstid(String instid) {
        this.instid = instid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

   /*
    public String getBooktime() {
        return booktime;
    }

    public void setBooktime(String booktime) {
        this.booktime = booktime;
    }*/

    public String getBookdate() {
        return bookdate;
    }

    public void setBookdate(String bookdate) {
        this.bookdate = bookdate;
    }

    public String getBookhrs() {
        return bookhrs;
    }

    public void setBookhrs(String bookhrs) {
        this.bookhrs = bookhrs;
    }

    public String getSts() {
        return sts;
    }

    public void setSts(String sts) {
        this.sts = sts;
    }

  /*  public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }*/

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }
}
