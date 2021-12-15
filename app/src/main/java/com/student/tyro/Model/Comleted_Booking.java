package com.student.tyro.Model;


public class Comleted_Booking {

    String id, sid, instid, instname, language, pic, uid, location, strt_tme, end_tme, bookdate, bookhrs, start_lat, start_long, end_lat, end_long, total_travel, spped, total_tme, avg_rate,amount,sts;
String ratingStatus;
    public Comleted_Booking(String id, String sid, String instid, String instname, String language,
                            String pic, String uid, String location, String strt_tme, String end_tme,
                            String bookdate, String bookhrs, String start_lat, String start_long,
                            String end_lat, String end_long, String total_travel, String spped,
                            String total_tme, String avg_rate, String amt, String sts, String ratingStatus) {
        this.id = id;
        this.sid = sid;
        this.instid = instid;
        this.instname = instname;
        this.language = language;
        this.pic = pic;
        this.uid = uid;
        this.location = location;
        this.strt_tme = strt_tme;
        this.end_tme = end_tme;
        this.bookdate = bookdate;
        this.bookhrs = bookhrs;
        this.start_lat = start_lat;
        this.start_long = start_long;
        this.end_lat = end_lat;
        this.end_long = end_long;
        this.total_travel = total_travel;
        this.spped = spped;
        this.total_tme = total_tme;
        this.avg_rate = avg_rate;
        this.amount = amt;
        this.sts = sts;
        this.ratingStatus = ratingStatus;
    }

    public String getRatingStatus() {
        return ratingStatus;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getInstname() {
        return instname;
    }

    public void setInstname(String instname) {
        this.instname = instname;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getStart_lat() {
        return start_lat;
    }

    public void setStart_lat(String start_lat) {
        this.start_lat = start_lat;
    }

    public String getStart_long() {
        return start_long;
    }

    public void setStart_long(String start_long) {
        this.start_long = start_long;
    }

    public String getEnd_lat() {
        return end_lat;
    }

    public void setEnd_lat(String end_lat) {
        this.end_lat = end_lat;
    }

    public String getEnd_long() {
        return end_long;
    }

    public void setEnd_long(String end_long) {
        this.end_long = end_long;
    }

    public String getTotal_travel() {
        return total_travel;
    }

    public void setTotal_travel(String total_travel) {
        this.total_travel = total_travel;
    }

    public String getSpped() {
        return spped;
    }

    public void setSpped(String spped) {
        this.spped = spped;
    }

    public String getTotal_tme() {
        return total_tme;
    }

    public void setTotal_tme(String total_tme) {
        this.total_tme = total_tme;
    }

    public String getAvg_rate() {
        return avg_rate;
    }

    public void setAvg_rate(String avg_rate) {
        this.avg_rate = avg_rate;
    }

    public String getSts() {
        return sts;
    }

    public void setSts(String sts) {
        this.sts = sts;
    }
}