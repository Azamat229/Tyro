package com.student.tyro.Model;

public class SavedCardModel
{
    String id,user_id,card_name,card_number,card_expiry,card_cvv,card_type,card_pincode,createdon;

public SavedCardModel(String id, String user_id, String card_name, String card_number, String card_expiry, String card_type,String card_cvv,String card_pincode,String created) {
        this.id = id;
        this.user_id = user_id;
        this.card_name = card_name;
        this.card_number = card_number;
        this.card_expiry = card_expiry;
        this.card_type = card_type;
        this.card_cvv = card_cvv;
        this.card_pincode = card_pincode;
        this.createdon=created;
        }

        public String getCreatedon() {
                return createdon;
        }

        public void setCreatedon(String createdon) {
                this.createdon = createdon;
        }

        public String getId() {
        return id;
        }

public void setId(String id) {
        this.id = id;
        }

public String getUser_id() {
        return user_id;
        }

public void setUser_id(String user_id) {
        this.user_id = user_id;
        }

public String getCard_name() {
        return card_name;
        }

public void setCard_name(String card_name) {
        this.card_name = card_name;
        }

public String getCard_number() {
        return card_number;
        }

public void setCard_number(String card_number) {
        this.card_number = card_number;
        }

public String getCard_expiry() {
        return card_expiry;
        }

public void setCard_expiry(String card_expiry) {
        this.card_expiry = card_expiry;
        }

public String getCard_cvv() {
        return card_cvv;
        }

public void setCard_cvv(String card_cvv) {
        this.card_cvv = card_cvv;
        }

public String getCard_type() {
        return card_type;
        }

public void setCard_type(String card_type) {
        this.card_type = card_type;
        }

public String getCard_pincode() {
        return card_pincode;
        }

public void setCard_pincode(String card_pincode) {
        this.card_pincode = card_pincode;
        }

}
