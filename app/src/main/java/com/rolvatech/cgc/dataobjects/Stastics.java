package com.rolvatech.cgc.dataobjects;

import java.io.Serializable;

public class Stastics implements Serializable {
    private String CHILD;

    private String STAFF;

    private String AREAS;

    private String REPORT;

    public Stastics(String CHILD, String STAFF, String AREAS, String REPORT) {
        this.CHILD = CHILD;
        this.STAFF = STAFF;
        this.AREAS = AREAS;
        this.REPORT = REPORT;
    }

    public String getCHILD() {
        return CHILD;
    }

    public void setCHILD(String CHILD) {
        this.CHILD = CHILD;
    }

    public String getSTAFF() {
        return STAFF;
    }

    public void setSTAFF(String STAFF) {
        this.STAFF = STAFF;
    }

    public String getAREAS() {
        return AREAS;
    }

    public void setAREAS(String AREAS) {
        this.AREAS = AREAS;
    }

    public String getREPORT() {
        return REPORT;
    }

    public void setREPORT(String REPORT) {
        this.REPORT = REPORT;
    }
}
