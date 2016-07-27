package com.at.solcoal.data;

public class FeedItem {
    private String title;
    private String thumbnail;
    private String overflow;
    private String shop_id;
    private String shop_desc;
    private String shop_contact_email;

    public String getShop_phone_no() {
        return shop_phone_no;
    }

    public void setShop_phone_no(String shop_phone_no) {
        this.shop_phone_no = shop_phone_no;
    }

    private String shop_phone_no;

    public String getShop_contact_email() {
        return shop_contact_email;
    }

    public void setShop_contact_email(String shop_contact_email) {
        this.shop_contact_email = shop_contact_email;
    }



    public String getShop_desc() {
        return shop_desc;
    }

    public void setShop_desc(String shop_desc) {
        this.shop_desc = shop_desc;
    }



    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }


    public String getOverflow() {
        return overflow;
    }

    public void setOverflow(String overflow) {
        this.overflow = overflow;
    }

}
