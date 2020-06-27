package com.rolvatech.cgc;

import java.io.Serializable;

public class Tasks implements Serializable {
    private Long id;

    private String taskName;

    private Long userTaskId;

    private Integer noofLevels;

    private String criteriaDesc;

    private long areaId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Long getUserTaskId() {
        return userTaskId;
    }

    public void setUserTaskId(Long userTaskId) {
        this.userTaskId = userTaskId;
    }

    public Integer getNoofLevels() {
        return noofLevels;
    }

    public void setNoofLevels(Integer noofLevels) {
        this.noofLevels = noofLevels;
    }

    public String getCriteriaDesc() {
        return criteriaDesc;
    }

    public void setCriteriaDesc(String criteriaDesc) {
        this.criteriaDesc = criteriaDesc;
    }

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }
}
