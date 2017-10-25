package com.sunkoiwish.waphotorecon.Models;

/**
 * Created by Xaiyeon on 10/24/2017.
 */

public class Photo {

    private String photo_id;
    private String photoName;
    private String imgdata_url;
    private String photo_description;
    private String photo_create_date;

    public Photo(){}

    public Photo(String photo_id, String photoName, String imgdata_url, String photo_description, String photo_create_date) {
        this.photo_id = photo_id;
        this.photoName = photoName;
        this.imgdata_url = imgdata_url;
        this.photo_description = photo_description;
        this.photo_create_date = photo_create_date;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getImgdata_url() {
        return imgdata_url;
    }

    public void setImgdata_url(String imgdata_url) {
        this.imgdata_url = imgdata_url;
    }

    public String getPhoto_description() {
        return photo_description;
    }

    public void setPhoto_description(String photo_description) {
        this.photo_description = photo_description;
    }

    public String getPhoto_create_date() {
        return photo_create_date;
    }

    public void setPhoto_create_date(String photo_create_date) {
        this.photo_create_date = photo_create_date;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "photo_id='" + photo_id + '\'' +
                ", photoName='" + photoName + '\'' +
                ", imgdata_url='" + imgdata_url + '\'' +
                ", photo_description='" + photo_description + '\'' +
                ", photo_create_date='" + photo_create_date + '\'' +
                '}';
    }
}
