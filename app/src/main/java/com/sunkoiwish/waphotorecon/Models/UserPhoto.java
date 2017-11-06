package com.sunkoiwish.waphotorecon.Models;

/**
 * Created by Xaiyeon on 10/27/2017.
 */

public class UserPhoto {

    private String user_photo_id;
    private String user_id;
    private String user_name;
    private String photoName;
    private String imgdata_url;
    private String taken_location;
    private String location_name;
    private String photo_description;
    private String photo_create_date;

    public UserPhoto() {
    }

    public UserPhoto(String user_photo_id, String user_id, String user_name, String photoName, String imgdata_url, String taken_location, String location_name, String photo_description, String photo_create_date) {
        this.user_photo_id = user_photo_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.photoName = photoName;
        this.imgdata_url = imgdata_url;
        this.taken_location = taken_location;
        this.location_name = location_name;
        this.photo_description = photo_description;
        this.photo_create_date = photo_create_date;
    }

    public String getUser_photo_id() {
        return user_photo_id;
    }

    public void setUser_photo_id(String user_photo_id) {
        this.user_photo_id = user_photo_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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

    public String getTaken_location() {
        return taken_location;
    }

    public void setTaken_location(String taken_location) {
        this.taken_location = taken_location;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
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
        return "UserPhoto{" +
                "user_photo_id='" + user_photo_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", photoName='" + photoName + '\'' +
                ", imgdata_url='" + imgdata_url + '\'' +
                ", taken_location='" + taken_location + '\'' +
                ", location_name='" + location_name + '\'' +
                ", photo_description='" + photo_description + '\'' +
                ", photo_create_date='" + photo_create_date + '\'' +
                '}';
    }
}
