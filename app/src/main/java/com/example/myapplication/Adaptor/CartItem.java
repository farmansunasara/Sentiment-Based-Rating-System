package com.example.myapplication.Adaptor;

import android.graphics.Bitmap;

public class CartItem {

    private int cartItemId; // New field to store the ID of the cart item
    private Bitmap coverImage;
    private String productName;
    private double productPrice;
    private int productQuantity;
    private double productSubtotal;

    // Updated constructor with ID parameter
    public CartItem(int cartItemId, Bitmap coverImage, String productName, double productPrice, int productQuantity, double productSubtotal) {
        this.cartItemId = cartItemId;
        this.coverImage = coverImage;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productSubtotal = productSubtotal;
    }

    // Getter method for cart item ID
    public int getCartItemId() {
        return cartItemId;
    }

    public Bitmap getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(Bitmap coverImage) {
        this.coverImage = coverImage;
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
