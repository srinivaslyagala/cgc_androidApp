package com.rolvatech.cgc.dataobjects;


import java.io.Serializable;
import java.util.List;

public class StaffDTO implements Serializable {


    private Long id;

    private String firstname;

    private String lastName;

    private String userName;

    private String contact;

    private String email;

    private String assignedChildCount;

    private String profileImage;

    private List<String> roles;

    private String password;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstName) {
        this.firstname = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssignedChildCount() {
        return assignedChildCount;
    }

    public void setAssignedChildCount(String assignedChildCount) {
        this.assignedChildCount = assignedChildCount;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
