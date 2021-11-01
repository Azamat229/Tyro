package com.student.tyro.Model;

public class ChatModel {

    String id,senderid,receiverid,msg,readstatus,addedat,userimg,firstname,recename,receimg,udatedat;


    public ChatModel(String id, String senderid, String receiverid, String msg, String readstatus, String addedat, String userimg, String firstname, String rece_name, String rece_img, String udatedat) {
        this.id = id;
        this.senderid = senderid;
        this.receiverid = receiverid;
        this.msg = msg;
        this.readstatus = readstatus;
        this.addedat = addedat;
        this.userimg = userimg;
        this.firstname = firstname;
        this.recename = rece_name;
        this.receimg = rece_img;
        this.udatedat = udatedat;

    }

    public String getRecename() {
        return recename;
    }

    public void setRecename(String recename) {
        this.recename = recename;
    }

    public String getReceimg() {
        return receimg;
    }

    public void setReceimg(String receimg) {
        this.receimg = receimg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(String receiverid) {
        this.receiverid = receiverid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getReadstatus() {
        return readstatus;
    }

    public void setReadstatus(String readstatus) {
        this.readstatus = readstatus;
    }

    public String getAddedat() {
        return addedat;
    }

    public void setAddedat(String addedat) {
        this.addedat = addedat;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getUdatedat() {
        return udatedat;
    }

    public void setUdatedat(String udatedat) {
        this.udatedat = udatedat;
    }
}
