
package com.rolvatech.cgc.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskList {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("areaNumber")
    @Expose
    private Object areaNumber;
    @SerializedName("areaId")
    @Expose
    private Integer areaId;
    @SerializedName("tasks")
    @Expose
    private List<Task> tasks = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getAreaNumber() {
        return areaNumber;
    }

    public void setAreaNumber(Object areaNumber) {
        this.areaNumber = areaNumber;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

}
