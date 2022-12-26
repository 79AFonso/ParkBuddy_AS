package com.example.parkbuddy;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String userId;

    public Upload(){
        //empty constructor needed
    }
    public Upload(String name, String imageUrl,String userId){
        if(name.trim().equals("")){
            name = "No Name";
        }
        mName = name;
        mImageUrl = imageUrl;
        userId = userId;
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
}
