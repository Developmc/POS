package com.example.multiable.pos.Data;

/**
 * Created by macremote on 2015/11/2.
 * 用于记录GridView中项的信息（每一项的图标和名称）
 */
public class GridViewItemInfo {
    private String name ;
    //记录logo的图片资源id(int 类型)
    private int logo ;
    public GridViewItemInfo(){}
    public GridViewItemInfo(String name,int logo){
        this.name=name;
        this.logo=logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }
}
