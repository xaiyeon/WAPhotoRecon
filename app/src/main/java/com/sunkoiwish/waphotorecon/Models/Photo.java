package com.sunkoiwish.waphotorecon.Models;

/**
 * Created by Xaiyeon on 10/24/2017.
 */

public class Photo {

    String photoID;
    String photoName;
    String imgDataURL;
    String photoDescription;
    String photoCreateDate;

    public Photo(){}

    public Photo(String photoID, String photoName, String imgDataURL, String photoDescription, String photoCreateDate) {
        this.photoID = photoID;
        this.photoName = photoName;
        this.imgDataURL = imgDataURL;
        this.photoDescription = photoDescription;
        this.photoCreateDate = photoCreateDate;
    }

    public String getPhotoID() {
        return photoID;
    }

    public String getPhotoName() {
        return photoName;
    }

    public String getImgDataURL() {
        return imgDataURL;
    }

    public String getPhotoDescription() {
        return photoDescription;
    }

    public String getPhotoCreateDate() {
        return photoCreateDate;
    }

    public void setPhotoID(String photoID) {
        this.photoID = photoID;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public void setImgDataURL(String imgDataURL) {
        this.imgDataURL = imgDataURL;
    }

    public void setPhotoDescription(String photoDescription) {
        this.photoDescription = photoDescription;
    }

    public void setPhotoCreateDate(String photoCreateDate) {
        this.photoCreateDate = photoCreateDate;
    }
}
