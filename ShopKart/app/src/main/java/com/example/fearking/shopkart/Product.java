
package com.example.fearking.shopkart;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by root on 09-02-2017.
 */

public class Product  implements Parcelable {
   @com.google.gson.annotations.SerializedName("id")
    private int id;
    @com.google.gson.annotations.SerializedName("name")
    private String name;
    @com.google.gson.annotations.SerializedName("price")
    private int price;
    @com.google.gson.annotations.SerializedName("description")
    private String description;
    @com.google.gson.annotations.SerializedName("category_id")
    private int categoryId;
    @com.google.gson.annotations.SerializedName("category_name")
    private String categoryName;
    @com.google.gson.annotations.SerializedName("retailer_id")
    private int retailerId;
    @com.google.gson.annotations.SerializedName("retailer_name")
    private String retailerName;
    @com.google.gson.annotations.SerializedName("rating")
    private double rating;
    @com.google.gson.annotations.SerializedName("no_of_rating")
    private int noOfRating;
    @com.google.gson.annotations.SerializedName("stock")
    private int stock;
    @com.google.gson.annotations.SerializedName("quantity")
    private int quantity;
    @com.google.gson.annotations.SerializedName("confirmation_number")
    private long confirmationNumber;
    @com.google.gson.annotations.SerializedName("status")
    private String status;





    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product(int id, String name, int price, String description, int categoryId, String categoryName, int retailerId, String retailerName, double rating, int noOfRating, int stock, @Nullable int quantity,@Nullable long confirmationNumber,@Nullable String status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.retailerId = retailerId;
        this.retailerName = retailerName;
        this.rating = rating;
        this.noOfRating = noOfRating;
        this.stock = stock;
        this.quantity=quantity;
        this.confirmationNumber=confirmationNumber;

    }
    public long getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(int confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public int getNoOfRating() {
        return noOfRating;
    }

    public void setNoOfRating(int noOfRating) {
        this.noOfRating = noOfRating;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(int retailerId) {
        this.retailerId = retailerId;
    }

    public String getRetailerName() {
        return retailerName;
    }

    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
    }
    public void incrementQuantity(){
        quantity++;
    }
    public void decrementQuantity(){
        quantity--;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.price);
        dest.writeString(this.description);
        dest.writeInt(this.categoryId);
        dest.writeString(this.categoryName);
        dest.writeInt(this.retailerId);
        dest.writeString(this.retailerName);
        dest.writeDouble(this.rating);
        dest.writeInt(this.noOfRating);
        dest.writeInt(this.stock);
        dest.writeInt(this.quantity);
        dest.writeLong(this.confirmationNumber);
        dest.writeString(this.status);
    }

    public Product() {
        this.quantity=1;
    }



    protected Product(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.price = in.readInt();
        this.description = in.readString();
        this.categoryId = in.readInt();
        this.categoryName = in.readString();
        this.retailerId = in.readInt();
        this.retailerName = in.readString();
        this.rating=in.readDouble();
        this.noOfRating=in.readInt();
        this.stock=in.readInt();
        this.quantity=in.readInt();
        this.confirmationNumber=in.readLong();
        this.status=in.readString();
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getStatus() {
        return status;
    }
    public void setStatus(String status){
        this.status=status;
    }
}
