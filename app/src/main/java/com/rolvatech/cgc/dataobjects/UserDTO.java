package com.rolvatech.cgc.dataobjects;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class UserDTO implements Serializable, Cloneable {

    private Long id;

    private String firstName;

    private String lastName;

    private String userName;

    private String email;

    private String contact;

    private String password;

    private String occupation;

    private String aboutMe;

    private String parentName;

    private String timeSlot;

    private Integer age;

    private Long assignedChildCount;

    private InstituteDTO institute;

    private StaffDTO staff;

    private String profileImage;

    private Boolean staffAssigned;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public InstituteDTO getInstitute() {
        return institute;
    }

    public void setInstitute(InstituteDTO institute) {
        this.institute = institute;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StaffDTO getStaff() {
        return staff;
    }

    public void setStaff(StaffDTO staff) {
        this.staff = staff;
    }

    public Long getAssignedChildCount() {
        return assignedChildCount;
    }

    public void setAssignedChildCount(Long assignedChildCount) {
        this.assignedChildCount = assignedChildCount;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Boolean getStaffAssigned() {
        return staffAssigned;
    }

    public void setStaffAssigned(Boolean staffAssigned) {
        this.staffAssigned = staffAssigned;
    }
}
