package com.sunkoiwish.waphotorecon.Models;

/**
 * Created by Xaiyeon on 10/24/2017.
 */

public class User {

    private String _user_id;
    private String user_uid;
    private String email_name;
    private String display_name;
    private boolean verified_account;

    public User() {
    }

    public User(String _user_id, String user_uid, String email_name, String display_name, boolean verified_account) {
        this._user_id = _user_id;
        this.user_uid = user_uid;
        this.email_name = email_name;
        this.display_name = display_name;
        this.verified_account = verified_account;
    }

    public String get_user_id() {
        return _user_id;
    }

    public void set_user_id(String _user_id) {
        this._user_id = _user_id;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getEmail_name() {
        return email_name;
    }

    public void setEmail_name(String email_name) {
        this.email_name = email_name;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public boolean isVerified_account() {
        return verified_account;
    }

    public void setVerified_account(boolean verified_account) {
        this.verified_account = verified_account;
    }

    @Override
    public String toString() {
        return "User{" +
                "_user_id='" + _user_id + '\'' +
                ", user_uid='" + user_uid + '\'' +
                ", email_name='" + email_name + '\'' +
                ", display_name='" + display_name + '\'' +
                ", verified_account=" + verified_account +
                '}';
    }
}
