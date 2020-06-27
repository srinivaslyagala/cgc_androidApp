package com.rolvatech.cgc.dataobjects;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;



import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;


public class InstituteDTO implements Serializable {

    private Long id;

    private String name;

    private String contact;

    private String email;

    private String address;

    private String accountValidTill;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccountValidTill() {
        return accountValidTill;
    }

    public void setAccountValidTill(String accountValidTill) {
        this.accountValidTill = accountValidTill;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
