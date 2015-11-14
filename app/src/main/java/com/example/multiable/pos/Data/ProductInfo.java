package com.example.multiable.pos.Data;

/**
 * Created by macremote on 2015/10/28.
 */
public class ProductInfo {

    private int item ;   //item不可用
    private String name ;
    private String size ;
    private String unit ;
    private double qty ;
    private double unitPrice ;
    private double disc ;
    private double totalPrice ;
    public ProductInfo(){

    }
    public ProductInfo(String size,String name,String unit,double qty,
                       double unitPrice,double disc,double totalPrice){
        this.name = name ;
        this.size = size ;
        this.unit = unit ;
        this.qty = qty ;
        this.unitPrice = unitPrice ;
        this.disc = disc ;
        this.totalPrice = totalPrice ;
    }
    public int getItem(){
        return item ;
    }
    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public String getUnit() {
        return unit;
    }

    public double getQty() {
        return qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getDisc() {
        return disc;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
    public void setItem(int item) {
        this.item = item;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setDisc(double disc) {
        this.disc = disc;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

}
