
package com.example.fearking.shopkart;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 10-02-2017.
 */

public class CartItem implements Parcelable {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private int price;
    @SerializedName("retailer_id")
    private int retailerId;
    @SerializedName("retailer_name")
    private String retailerName;
    @SerializedName("stock")
    private int stock;
    @SerializedName("quantity")
    private int quantity;


    public CartItem(int quantity, String name, int price, int retailerId, String retailerName, int stock, int id) {
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.retailerId = retailerId;
        this.retailerName = retailerName;
        this.stock = stock;
        this.id = id;
    }
    public CartItem(Product product){
        this.quantity = 1;
        this.name = product.getName();
        this.price = product.getPrice();
        this.retailerId = product.getRetailerId();
        this.retailerName = product.getRetailerName();
        this.stock = product.getStock();
        this.id = product.getId();
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
        dest.writeInt(this.retailerId);
        dest.writeString(this.retailerName);
        dest.writeInt(this.stock);
        dest.writeInt(this.quantity);
    }

    public CartItem() {
    }

    protected CartItem(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.price = in.readInt();
        this.retailerId = in.readInt();
        this.retailerName = in.readString();
        this.stock = in.readInt();
        this.quantity = in.readInt();
    }

    public static final Parcelable.Creator<CartItem> CREATOR = new Parcelable.Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel source) {
            return new CartItem(source);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };
}
