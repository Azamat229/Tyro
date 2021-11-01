package com.student.tyro.Model;

public class Covid_msgs {

    String id,catid,msgs,sts,created;

    public Covid_msgs(String id, String catid, String msgs, String sts, String created) {
        this.id = id;
        this.catid = catid;
        this.msgs = msgs;
        this.sts = sts;
        this.created = created;
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

    public String getMsgs() {
        return msgs;
    }

    public void setMsgs(String msgs) {
        this.msgs = msgs;
    }

    public String getSts() {
        return sts;
    }

    public void setSts(String sts) {
        this.sts = sts;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
