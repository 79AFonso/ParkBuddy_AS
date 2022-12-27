package com.example.parkbuddy;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

@IgnoreExtraProperties
public class VehicleModel {

    @PropertyName("id")
    private String mId;
    @PropertyName("model")
    private String mModel;
    @PropertyName("plate")
    private String mPlate;
    @PropertyName("imageUrl")
    private String mImageUrl;
    @PropertyName("userId")
    private String mUserId;

    public VehicleModel() {
        // Default constructor required for calls to DataSnapshot.getValue(VehicleModel.class)
    }

    public VehicleModel(String id, String plate, String model, String imageUrl, String userId) {
        mId = id;
        mPlate = plate;
        mModel = model;
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

    @PropertyName("model")
    public String getModel() {
        return mModel;
    }

    @PropertyName("model")
    public void setModel(String model) {
        mModel = model;
    }

    @PropertyName("plate")
    public String getPlate() {
        return mPlate;
    }

    @PropertyName("plate")
    public void setPlate(String plate) {
        mPlate = plate;
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
