package com.example.parkbuddy;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String userId;
    private String mmodel;

    public Upload(){
        //empty constructor needed
    }
    public Upload(String name, String imageUrl,String user,String model){
        if(name.trim().equals("")){
            name = "No Name";
        }
        mName = name;
        mImageUrl = imageUrl;
        userId = user;
        mmodel = model;
    }

    public Upload(String plate, String currentUser, String model) {
        mName = plate;
        userId = currentUser;
        mmodel = model;
    }

    public String getName() {
        return mName;
    }
    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getModel() {
        return mmodel;
    }
    public void setModel(String model) {
        this.mmodel = model;
    }
}
