package com.example.multiable.pos.Data;

/**
 * Created by macremote on 2015/10/31.
 */
public class ProductInventoryInfo {
    private String name ;
    private String desc ;
    private int twShop ;
    private int tstShop ;
    private int totalQty ;
    private String unit ;
    public ProductInventoryInfo(){}
    public ProductInventoryInfo(String name,String desc,int twShop,int tstShop,int totalQty,String unit){
        this.name = name;
        this.desc = desc;
        this.twShop = twShop;
        this.tstShop = tstShop;
        this.totalQty = totalQty;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getTwShop() {
        return twShop;
    }

    public void setTwShop(int twShop) {
        this.twShop = twShop;
    }

    public int getTstShop() {
        return tstShop;
    }

    public void setTstShop(int tstShop) {
        this.tstShop = tstShop;
    }

    public int getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(int totalQty) {
        this.totalQty = totalQty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
