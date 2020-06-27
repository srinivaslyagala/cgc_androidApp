package com.rolvatech.cgc.dataobjects;

import java.io.Serializable;

public class Child implements Serializable {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String contact;

    private String occupation;

    private String parentName;

    private String age;

    private Boolean staffAssigned;

    private String profileImage;

    private String timeSlot;

    private StaffDTO staff;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public boolean getStaffAssigned() {
        return staffAssigned;
    }

    public void setStaffAssigned(Boolean staffAssigned) {
        this.staffAssigned = staffAssigned;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public StaffDTO getStaff() {
        return staff;
    }

    public void setStaff(StaffDTO staff) {
        this.staff = staff;
    }
}
