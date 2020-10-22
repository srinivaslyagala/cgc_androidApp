package com.rolvatech.cgc.utils;

import com.rolvatech.cgc.dataobjects.Child;
import com.rolvatech.cgc.dataobjects.StaffDTO;
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

    public static void transformUserDtoToStaff(UserDTO userDto, UserDTO staffDto){
        if(userDto!=null ) {
            staffDto = new UserDTO();
            staffDto.setId(userDto.getId());
            staffDto.setFirstName(userDto.getFirstName());
            staffDto.setLastName(userDto.getLastName());
            staffDto.setContact(userDto.getContact());
            staffDto.setEmail(userDto.getEmail());
            staffDto.setProfileImage(userDto.getProfileImage());
        }
    }


}


