package com.sunkoiwish.waphotorecon.Models;

/**
 * Created by Royce on 11/15/2017.
 */

// Used for the user's analyzed photos

    // TODO: Finish this class model.
    // Maybe look for a java library for turning JSON into Java class model
public class UserAnalyzePhoto {

    private String user_analyze_photo_id; // primary key
    private String user_id; // foreign key, from user auth
    private String photo_id; // foreign key, from photo database
    // below is basically Strings from the JSON object.
}
