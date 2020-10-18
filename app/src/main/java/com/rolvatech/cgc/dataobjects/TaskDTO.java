package com.rolvatech.cgc.dataobjects;

import java.io.Serializable;
import java.util.List;


public class TaskDTO implements Serializable {


    private Long id;

    private String taskName;

    private Long userTaskId;

    private Integer noofLevels;

    private String criteriaDesc;

    private AreaDTO area;

    private String status;

    private Integer level;

    private List<SubTaskDTO> subTasks;

    public Long getId() {
        return id;
    }

    public void setId(Long taskNumber) {
        this.id = taskNumber;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
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

    public AreaDTO getArea() {
        return area;
    }

    public void setArea(AreaDTO area) {
        this.area = area;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<SubTaskDTO> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<SubTaskDTO> subTasks) {
        this.subTasks = subTasks;
    }

    public Long getUserTaskId() {
        return userTaskId;
    }

    public void setUserTaskId(Long userTaskId) {
        this.userTaskId = userTaskId;
    }
    @Override
    public String toString() {
        return "TaskDTO{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", userTaskId=" + userTaskId +
                ", noofLevels=" + noofLevels +
                ", criteriaDesc='" + criteriaDesc + '\'' +
                ", status='" + status + '\'' +
                ", level=" + level +
                ", subTasks=" + subTasks +
                '}';
    }

}
