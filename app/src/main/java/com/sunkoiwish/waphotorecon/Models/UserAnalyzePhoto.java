package com.sunkoiwish.waphotorecon.Models;

/**
 * Created by Royce on 11/15/2017.
 */

// Used for the user's analyzed photos

    // TODO: Finish this class model.
    // Maybe look for a java library for turning JSON into Java class model
public class UserAnalyzePhoto {

    private String user_analyze_photo_id; // primary key
    private String display_name; // user display name
    private String user_id; // foreign key, from user auth
    private String photo_id; // foreign key, from photo database
    private String analyzed_image_url; // this is from the storage of the analyzed image
    private String is_analyzed; // becomes true
    private String status; // will be used for validity later on
    // below is basically Strings from the JSON object.
    private String tags; // for like what it think it is
    private String description;
    private String adult_content;
    private String racy_content;
    private String age;
    private String gender;
    private String smile;
    private String analyzed_date; // current time when analyzed
    private String image_status; // the seed, etc.
    private String search_date; // short date
    private String taken_location;
    private String device_name; // the device name


    public UserAnalyzePhoto() {
    }


    public UserAnalyzePhoto(String user_analyze_photo_id, String display_name, String user_id, String photo_id, String analyzed_image_url, String is_analyzed, String status, String tags, String description, String adult_content, String racy_content, String age, String gender, String smile, String analyzed_date, String image_status, String search_date, String taken_location, String device_name) {
        this.user_analyze_photo_id = user_analyze_photo_id;
        this.display_name = display_name;
        this.user_id = user_id;
        this.photo_id = photo_id;
        this.analyzed_image_url = analyzed_image_url;
        this.is_analyzed = is_analyzed;
        this.status = status;
        this.tags = tags;
        this.description = description;
        this.adult_content = adult_content;
        this.racy_content = racy_content;
        this.age = age;
        this.gender = gender;
        this.smile = smile;
        this.analyzed_date = analyzed_date;
        this.image_status = image_status;
        this.search_date = search_date;
        this.taken_location = taken_location;
        this.device_name = device_name;
    }


    public String getUser_analyze_photo_id() {
        return user_analyze_photo_id;
    }

    public void setUser_analyze_photo_id(String user_analyze_photo_id) {
        this.user_analyze_photo_id = user_analyze_photo_id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public String getAnalyzed_image_url() {
        return analyzed_image_url;
    }

    public void setAnalyzed_image_url(String analyzed_image_url) {
        this.analyzed_image_url = analyzed_image_url;
    }

    public String getIs_analyzed() {
        return is_analyzed;
    }

    public void setIs_analyzed(String is_analyzed) {
        this.is_analyzed = is_analyzed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdult_content() {
        return adult_content;
    }

    public void setAdult_content(String adult_content) {
        this.adult_content = adult_content;
    }

    public String getRacy_content() {
        return racy_content;
    }

    public void setRacy_content(String racy_content) {
        this.racy_content = racy_content;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSmile() {
        return smile;
    }

    public void setSmile(String smile) {
        this.smile = smile;
    }

    public String getAnalyzed_date() {
        return analyzed_date;
    }

    public void setAnalyzed_date(String analyzed_date) {
        this.analyzed_date = analyzed_date;
    }

    public String getImage_status() {
        return image_status;
    }

    public void setImage_status(String image_status) {
        this.image_status = image_status;
    }

    public String getSearch_date() {
        return search_date;
    }

    public void setSearch_date(String search_date) {
        this.search_date = search_date;
    }

    public String getTaken_location() {
        return taken_location;
    }

    public void setTaken_location(String taken_location) {
        this.taken_location = taken_location;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    @Override
    public String toString() {
        return "UserAnalyzePhoto{" +
                "user_analyze_photo_id='" + user_analyze_photo_id + '\'' +
                ", display_name='" + display_name + '\'' +
                ", user_id='" + user_id + '\'' +
                ", photo_id='" + photo_id + '\'' +
                ", analyzed_image_url='" + analyzed_image_url + '\'' +
                ", is_analyzed='" + is_analyzed + '\'' +
                ", status='" + status + '\'' +
                ", tags='" + tags + '\'' +
                ", description='" + description + '\'' +
                ", adult_content='" + adult_content + '\'' +
                ", racy_content='" + racy_content + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", smile='" + smile + '\'' +
                ", analyzed_date='" + analyzed_date + '\'' +
                ", image_status='" + image_status + '\'' +
                ", search_date='" + search_date + '\'' +
                ", taken_location='" + taken_location + '\'' +
                ", device_name='" + device_name + '\'' +
                '}';
    }
}
