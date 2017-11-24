package com.sunkoiwish.waphotorecon.Models;

/**
 * Created by Xaiyeon on 10/24/2017.
 */

public class Photo {

    private String photo_id;
    private String user_id;
    private String user_name;
    private String photoName;
    private String imgdata_url;
    private String taken_location;
    private String location_name;
    private String photo_description;
    private String photo_create_date;
    private String device_name; // used for the name of the device...
    private String isAnalyzed; // used for checking if photo has been analyzed already or not
    // false or true, all initial photos are set to false.

    public Photo() {
    }

    public Photo(String photo_id, String user_id, String user_name, String photoName, String imgdata_url, String taken_location, String location_name, String photo_description, String photo_create_date, String device_name, String isAnalyzed) {
        this.photo_id = photo_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.photoName = photoName;
        this.imgdata_url = imgdata_url;
        this.taken_location = taken_location;
        this.location_name = location_name;
        this.photo_description = photo_description;
        this.photo_create_date = photo_create_date;
        this.device_name = device_name;
        this.isAnalyzed = isAnalyzed;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
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

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getIsAnalyzed() {
        return isAnalyzed;
    }

    public void setIsAnalyzed(String isAnalyzed) {
        this.isAnalyzed = isAnalyzed;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "photo_id='" + photo_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", photoName='" + photoName + '\'' +
                ", imgdata_url='" + imgdata_url + '\'' +
                ", taken_location='" + taken_location + '\'' +
                ", location_name='" + location_name + '\'' +
                ", photo_description='" + photo_description + '\'' +
                ", photo_create_date='" + photo_create_date + '\'' +
                ", device_name='" + device_name + '\'' +
                ", isAnalyzed='" + isAnalyzed + '\'' +
                '}';
    }
}
