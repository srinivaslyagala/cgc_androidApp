package com.rolvatech.cgc.utils;

import com.rolvatech.cgc.dataobjects.Child;
import com.rolvatech.cgc.dataobjects.UserDTO;

public class UtilTransformer {
        public static void transformUserDtoToChild(UserDTO userDto,Child child){
            if(userDto!=null ) {
                child = new Child();
                child.setId(userDto.getId());
                child.setFirstName(userDto.getFirstName());
                child.setLastName(userDto.getLastName());
                child.setAge("" + userDto.getAge());
                child.setContact(userDto.getContact());
                child.setEmail(userDto.getEmail());
                child.setOccupation(userDto.getOccupation());
                child.setParentName(userDto.getParentName());
                child.setProfileImage(userDto.getProfileImage());
                child.setStaffAssigned(userDto.getStaffAssigned());
                child.setTimeSlot(userDto.getAboutMe());
                child.setStaff(userDto.getStaff());
                child.setAboutMe(userDto.getAboutMe());
            }
        }


}


