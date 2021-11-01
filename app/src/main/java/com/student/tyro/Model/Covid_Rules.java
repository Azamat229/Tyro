package com.student.tyro.Model;

public class Covid_Rules {

    String id,msg,sts,created_on;

    public Covid_Rules(String id, String msg, String sts, String created_on) {
        this.id = id;
        this.msg = msg;
        this.sts = sts;
        this.created_on = created_on;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSts() {
        return sts;
    }

    public void setSts(String sts) {
        this.sts = sts;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }
}
