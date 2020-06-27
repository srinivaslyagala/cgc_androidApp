package com.rolvatech.cgc.dataobjects;

import androidx.room.ColumnInfo;
import androidx.room.Entity;


import java.io.Serializable;
import java.util.Date;

public class SubTaskDTO implements Serializable {


    private Long id;

    private TaskDTO task;

    private String name;

    private String description;

    private String completedOn;

    private String status;


    public TaskDTO getTask() {
        return task;
    }

    public void setTask(TaskDTO task) {
        this.task = task;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(String completedOn) {
        this.completedOn = completedOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
