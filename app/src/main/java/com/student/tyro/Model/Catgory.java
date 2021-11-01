package com.student.tyro.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Catgory implements Serializable {
    String categoryname;
    ArrayList<Faqcatgories> faqcatgories;

    public Catgory(String categoryname, ArrayList<Faqcatgories> faqcatgories) {
        this.categoryname = categoryname;
        this.faqcatgories = faqcatgories;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public ArrayList<Faqcatgories> getFaqcatgories() {
        return faqcatgories;
    }

    public void setFaqcatgories(ArrayList<Faqcatgories> faqcatgories) {
        this.faqcatgories = faqcatgories;
    }
}
