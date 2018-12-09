package com.core.tools;

public class Model {
    long id;
    String categoryName;
    String brand;
    String productName;
    String size;
    String color;
    String gender;
    long price = 0L;
    int review;
    boolean instock;
    boolean refresh;
    boolean onsale;
    String createTime;
    String updateTime;
    public Model() {
    }

    public Model(long id, String categoryName, String brand, String productName, String size, String color,
                 String gender, long price, int review, boolean instock, boolean refresh, boolean onsale,
                 String createTime,String updateTime) {
        this.id = id;
        this.categoryName = categoryName;
        this.brand = brand;
        this.productName = productName;
        this.size = size;
        this.color = color;
        this.gender = gender;
        this.price = price;
        this.review = review;
        this.instock = instock;
        this.refresh = refresh;
        this.onsale = onsale;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public boolean isInstock() {
        return instock;
    }

    public void setInstock(boolean instock) {
        this.instock = instock;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public boolean isOnsale() {
        return onsale;
    }

    public void setOnsale(boolean onsale) {
        this.onsale = onsale;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
