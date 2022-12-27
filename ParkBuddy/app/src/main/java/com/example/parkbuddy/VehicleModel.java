package com.example.parkbuddy;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

@IgnoreExtraProperties
public class VehicleModel {

    @PropertyName("id")
    private String mId;
    @PropertyName("name")
    private String mModel;
    @PropertyName("type")
    private String mPlate;
    @PropertyName("imageUrl")
    private String mImageUrl;
    @PropertyName("userId")
    private String mUserId;

    public VehicleModel() {
        // Default constructor required for calls to DataSnapshot.getValue(VehicleModel.class)
    }

    public VehicleModel(String id, String name, String type, String imageUrl, String userId) {
        mId = id;
        mModel = name;
        mPlate = type;
        mImageUrl = imageUrl;
        mUserId = userId;
    }

    public VehicleModel(String img, String model, String plate, String uid) {
        mImageUrl = img;
        mModel = model;
        mPlate = plate;
        mUserId = uid;
    }

    public VehicleModel(String model, String plate) {
        mModel = model;
        mPlate = plate;
    }

    @PropertyName("id")
    public String getId() {
        return mId;
    }

    @PropertyName("id")
    public void setId(String id) {
        mId = id;
    }

    @PropertyName("name")
    public String getModel() {
        return mModel;
    }

    @PropertyName("name")
    public void setModel(String name) {
        mModel = name;
    }

    @PropertyName("type")
    public String getPlate() {
        return mPlate;
    }

    @PropertyName("type")
    public void setPlate(String type) {
        mPlate = type;
    }

    @PropertyName("imageUrl")
    public String getImageUrl() {
        return mImageUrl;
    }

    @PropertyName("imageUrl")
    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    @PropertyName("userId")
    public String getUserId() {
        return mUserId;
    }

    @PropertyName("userId")
    public void setUserId(String userId) {
        mUserId = userId;
    }
}
