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

    public SysMessage(){}

    public SysMessage(String sys_message_id, String user_id, String message, String device_name, String is_visible, String create_date) {
        this.sys_message_id = sys_message_id;
        this.user_id = user_id;
        this.message = message;
        this.device_name = device_name;
        this.is_visible = is_visible;
        this.create_date = create_date;
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

    @Override
    public String toString() {
        return "SysMessage{" +
                "sys_message_id='" + sys_message_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", message='" + message + '\'' +
                ", device_name='" + device_name + '\'' +
                ", is_visible='" + is_visible + '\'' +
                ", create_date='" + create_date + '\'' +
                '}';
    }


}
