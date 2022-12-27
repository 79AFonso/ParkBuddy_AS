package com.example.parkbuddy;

public class Upload {
    private String mPlate;
    private String mImageUrl;
    private String userId;
    private String mModel;

    public Upload(){
        //empty constructor needed
    }
    public Upload(String plate, String imageUrl,String user,String model){
        if(plate.trim().equals("")){
            plate = "No Name";
        }
        mPlate = plate;
        mImageUrl = imageUrl;
        userId = user;
        mModel = model;
    }

    public Upload(String plate, String currentUser, String model) {
        mPlate = plate;
        userId = currentUser;
        mModel = model;
    }

    public String getPlate() {
        return mPlate;
    }
    public void setPlate(String plate) {
        mPlate = plate;
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
        return mModel;
    }
    public void setModel(String model) {
        this.mModel = model;
    }
}
