package com.sunkoiwish.waphotorecon.Models;

/**
 * Created by Xaiyeon on 12/3/2017.
 *
 * SysMessage is a class model for storing and retrieving the information
 * send from the beaglebone black wireless in simple to read text format.
 * These are simple read-able messages...
 *
 */

public class SysMessage {

    private String sys_message_id; // primary UID
    private String user_id;
    private String message; // an actual string message
    private String device_name; // from what device it came from
    private String is_visible; // this is a true or false thing set. User can set it. Only get true to show. true is set by default.
    private String create_date; // when the message was created
    private String photo_id; // the photo id
    // also add the seed thing and search_date
    private String image_status; // image status is for picture that were taken continously from start
    // image_status can be called seed(rand letter a - z + rand number 0 - 100) and then seed(prev rand)_link(# in succession)
    // for images taken using the application on the phone they will always be seed
    private String search_date; // search date is short version just for the date. example: 12_05_2017

    public SysMessage(){}


    public SysMessage(String sys_message_id, String user_id, String message, String device_name, String is_visible, String create_date, String photo_id, String image_status, String search_date) {
        this.sys_message_id = sys_message_id;
        this.user_id = user_id;
        this.message = message;
        this.device_name = device_name;
        this.is_visible = is_visible;
        this.create_date = create_date;
        this.photo_id = photo_id;
        this.image_status = image_status;
        this.search_date = search_date;
    }


    public String getSys_message_id() {
        return sys_message_id;
    }

    public void setSys_message_id(String sys_message_id) {
        this.sys_message_id = sys_message_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getIs_visible() {
        return is_visible;
    }

    public void setIs_visible(String is_visible) {
        this.is_visible = is_visible;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
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

    @Override
    public String toString() {
        return "SysMessage{" +
                "sys_message_id='" + sys_message_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", message='" + message + '\'' +
                ", device_name='" + device_name + '\'' +
                ", is_visible='" + is_visible + '\'' +
                ", create_date='" + create_date + '\'' +
                ", photo_id='" + photo_id + '\'' +
                ", image_status='" + image_status + '\'' +
                ", search_date='" + search_date + '\'' +
                '}';
    }
}
