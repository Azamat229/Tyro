package com.student.tyro.Model;

public class TimeSlot {

    String id,instruct_id,strt_tme,end_tme,total,prce,sts;

    public TimeSlot(String id, String instruct_id,String strt_tme, String end_tme, String total,String prce,String sts) {
        this.id = id;
        this.instruct_id = instruct_id;
        //this.booking_dt = booking_dt;
        this.strt_tme = strt_tme;
        this.end_tme = end_tme;
        this.total = total;
        this.prce=prce;
        this.sts = sts;
    }

    public String getPrce() {
        return prce;
    }

    public void setPrce(String prce) {
        this.prce = prce;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstruct_id() {
        return instruct_id;
    }

    public void setInstruct_id(String instruct_id) {
        this.instruct_id = instruct_id;
    }

   /* public String getBooking_dt() {
        return booking_dt;
    }

    public void setBooking_dt(String booking_dt) {
        this.booking_dt = booking_dt;
    }*/

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

    public String getSts() {
        return sts;
    }

    public void setSts(String sts) {
        this.sts = sts;
    }
}
