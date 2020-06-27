package com.rolvatech.cgc.dataobjects;

import java.io.Serializable;
import java.util.List;


public class AreaTaskDTO implements Serializable {


    private String name;

    private Long areaNumber;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private List<TaskDTO> tasks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAreaNumber() {
        return areaNumber;
    }

    public void setAreaNumber(Long areaNumber) {
        this.areaNumber = areaNumber;
    }

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }
}
