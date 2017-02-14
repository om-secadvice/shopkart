
package com.example.fearking.shopkart;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by root on 12-02-2017.
 */

public class Order implements Parcelable {

    @SerializedName("cart")
    private ArrayList<Product> products;
    @SerializedName("amount")
    private double amount;
    @SerializedName("shipping_address")
    private String shipping_address;
    @SerializedName("card_number")
    private String card_number;
    @SerializedName("pincode")
    private String pincode;
    @SerializedName("customer_id")
    private int customer_id;
    @SerializedName("confirmation_number")
    private long confirmation_number;
    @SerializedName("date_created")
    private String dateCreated;
    @SerializedName("status")
    private String status;





    public Order() {
        this.confirmation_number =0;
        this.amount=0;
        this.shipping_address="registered_address";
        this.card_number="";
        this.pincode="";
        this.customer_id=0;
        this.products=null;
        this.dateCreated="";
        this.status="";
    }

    public Order(ArrayList<Product> products, double amount, String shipping_address, String card_number, String pincode, int customer_id, long confirmation_number,@Nullable String dateCreated, @Nullable String status) {
        this.products = products;
        this.amount = amount;
        this.shipping_address = shipping_address;
        this.card_number = card_number;
        this.pincode = pincode;
        this.customer_id = customer_id;
        this.confirmation_number = confirmation_number;
        this.dateCreated=dateCreated;
        this.status=dateCreated;
    }

    public Order(ArrayList<Product> products, String shipping_address, int customer_id, String pincode) {
        this.products = products;
        this.shipping_address = shipping_address;
        this.customer_id = customer_id;
        this.pincode = pincode;
    }
    public int getNoOfItems(){
        return products.size();
    }

    private void calculateAmount() {
        double total=0;
        try{
        for(int i=0;i<products.size();i++){
            total+=products.get(i).getPrice()*products.get(i).getQuantity();
        }
        amount=total;
        }catch (NullPointerException e){

        }
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public double getAmount() {
        if(products!=null)calculateAmount();
        return amount;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getShippingAddress() {
        return shipping_address;
    }

    public void setShippingAddress(String shipping_address) {
        this.shipping_address = shipping_address;
    }

    public String getCardNumber() {
        return card_number;
    }

    public void setCardNumber(String card_number) {
        this.card_number = card_number;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public int getCustomerId() {
        return customer_id;
    }

    public void setCustomerId(int customer_id) {
        this.customer_id = customer_id;
    }
    public long getConfirmationNumber() {
        return confirmation_number;
    }

    public void setConfirmationNumber(long confirmation_number) {
        this.confirmation_number = confirmation_number;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.products);
        dest.writeDouble(this.amount);
        dest.writeString(this.shipping_address);
        dest.writeString(this.card_number);
        dest.writeString(this.pincode);
        dest.writeInt(this.customer_id);
        dest.writeLong(this.confirmation_number);
        dest.writeString(this.status);
        dest.writeString(this.dateCreated);
    }

    protected Order(Parcel in) {
        this.products = in.createTypedArrayList(Product.CREATOR);
        this.amount = in.readDouble();
        this.shipping_address = in.readString();
        this.card_number = in.readString();
        this.pincode = in.readString();
        this.customer_id = in.readInt();
        this.confirmation_number = in.readLong();
        this.status=in.readString();
        this.dateCreated=in.readString();
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
