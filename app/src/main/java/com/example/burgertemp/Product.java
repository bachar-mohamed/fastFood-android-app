package com.example.burgertemp;

import android.widget.ImageView;

import java.io.Serializable;

public class Product implements Serializable {

    private int id;
    private String name;
    private String ingredients;
    private int orders;
    private String price;
    private int img_url;
    private int category;
    private ImageView image;
    private String weight;
    private String delay;
    private String calories;
    private String rating;
    public Product(int id, String name, int img_url,String ingredients,String price,int orders,String weight,String calories,String rating,String delay,int category){
        this.name=name;
        this.orders=orders;
        this.price=price;
        this.ingredients=ingredients;
        this.id=id;
        this.img_url=img_url;
        this.category=category;
        this.rating =rating;
        this.weight=weight;
        this.delay=delay;
        this.calories=calories;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImg_url() {
        return img_url;
    }

    public void setImg_url(int img_url) {
        this.img_url = img_url;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}


