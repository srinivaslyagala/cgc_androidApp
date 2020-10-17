package com.rolvatech.cgc.dataobjects;

import java.io.Serializable;
import java.util.List;


public class ChildTaskDTO implements Serializable {


    private Long id;

    private List<TaskDTO> tasks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }

}
