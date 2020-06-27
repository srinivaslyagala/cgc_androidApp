package com.rolvatech.cgc.dataobjects;

import androidx.room.ColumnInfo;
import androidx.room.Entity;


import java.io.Serializable;


public class JwtResponse implements Serializable {

    private final String jwttoken;


    private UserDTO userDetails;


    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    public JwtResponse(String jwttoken, UserDTO user) {
        this.jwttoken = jwttoken;
        this.userDetails = user;
    }

    public String getToken() {
        return this.jwttoken;
    }

    public UserDTO getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDTO userDetails) {
        this.userDetails = userDetails;
    }
}