package com.rolvatech.cgc.dataobjects;

import java.io.Serializable;

public class AreaDTO implements Serializable {



    private Long id;

    private String areaName;

    private String areaNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long areaNumber) {
        this.id = areaNumber;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaNumber() {
        return areaNumber;
    }

    public void setAreaNumber(String areaNumber) {
        this.areaNumber = areaNumber;
    }
}
