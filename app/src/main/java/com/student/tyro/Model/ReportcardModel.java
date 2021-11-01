package com.student.tyro.Model;

import java.util.ArrayList;

public class ReportcardModel {

    String categoryname;
    ArrayList<Reportcardsublist> sublist;

    public ReportcardModel(String categoryname, ArrayList<Reportcardsublist> sublist) {
        this.categoryname = categoryname;
        this.sublist = sublist;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public ArrayList<Reportcardsublist> getSublist() {
        return sublist;
    }

    public void setSublist(ArrayList<Reportcardsublist> sublist) {
        this.sublist = sublist;
    }
}
