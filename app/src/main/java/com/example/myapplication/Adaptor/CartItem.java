package com.example.myapplication.Adaptor;

import android.graphics.Bitmap;

public class CartItem {

    private int cartItemId;
    private Bitmap coverImage;
    private String coverImagePath;
    private String productName;
    private double productPrice;
    private int productQuantity;
    private double productSubtotal;


    public CartItem(int cartItemId, Bitmap coverImage, String coverImagePath, String productName, double productPrice, int productQuantity, double productSubtotal) {
        this.cartItemId = cartItemId;
        this.coverImage = coverImage;
        this.coverImagePath = coverImagePath;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productSubtotal = productSubtotal;
    }

    // Getter method for cart item ID
    public int getCartItemId() {
        return cartItemId;
    }

    public String getCoverImagePath() {
        return coverImagePath;
    }

    // Setter method for cover image path
    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public double getProductSubtotal() {
        return productSubtotal;
    }

    public void setProductSubtotal(double productSubtotal) {
        this.productSubtotal = productSubtotal;
    }
}
