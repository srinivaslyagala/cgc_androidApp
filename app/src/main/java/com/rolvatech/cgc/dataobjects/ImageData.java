package com.rolvatech.cgc.dataobjects;

import java.io.Serializable;

public class ImageData implements Serializable {

    private String imageData;

    private Long userId;

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
