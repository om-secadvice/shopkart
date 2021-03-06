
package com.example.fearking.shopkart;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 09-02-2017.
 */

public class User implements Parcelable {

    @com.google.gson.annotations.SerializedName("id")
    private int id;
    @com.google.gson.annotations.SerializedName("name")
    private String name;
    @com.google.gson.annotations.SerializedName("username")
    private String username;
    @com.google.gson.annotations.SerializedName("password")
    private String password;
    @com.google.gson.annotations.SerializedName("phone")
    private String phone;
    @com.google.gson.annotations.SerializedName("email")
    private String email;
    @com.google.gson.annotations.SerializedName("address_line_1")
    private String addressLine1;
    @com.google.gson.annotations.SerializedName("address_line_2")
    private String addressLine2;
    @com.google.gson.annotations.SerializedName("landmark")
    private String landmark;
    @com.google.gson.annotations.SerializedName("city")
    private String city;
    @com.google.gson.annotations.SerializedName("state")
    private String state;
    @com.google.gson.annotations.SerializedName("pincode")
    private String pincode;


    public User(int id, String name, String username,String password, String phone, String email, String addressLine1, String addressLine2, String landmark, String city, String state, String pincode) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password=password;
        this.phone = phone;
        this.email = email;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.landmark = landmark;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getShippingAddress(){
        return addressLine1+" "+addressLine2+" "+landmark+" "+city+" "+state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.phone);
        dest.writeString(this.email);
        dest.writeString(this.addressLine1);
        dest.writeString(this.addressLine2);
        dest.writeString(this.landmark);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeString(this.pincode);

    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.username = in.readString();
        this.password=in.readString();
        this.phone = in.readString();
        this.email = in.readString();
        this.addressLine1 = in.readString();
        this.addressLine2 = in.readString();
        this.landmark = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.pincode = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
