package com.core.reflection.bytecode;

public abstract class AbstractShape implements Shape{

    private String desc;
    private int perimeter;
    private float acreage;

    public AbstractShape() {}

    public AbstractShape(String desc, int perimeter, float acreage) {
        this.desc = desc;
        this.perimeter = perimeter;
        this.acreage = acreage;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPerimeter() {
        return perimeter;
    }

    public void setPerimeter(int perimeter) {
        this.perimeter = perimeter;
    }

    public float getAcreage() {
        return acreage;
    }

    public void setAcreage(float acreage) {
        this.acreage = acreage;
    }
}
