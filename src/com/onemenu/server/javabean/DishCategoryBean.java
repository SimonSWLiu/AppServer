package com.onemenu.server.javabean;

import java.util.ArrayList;
import java.util.List;

public class DishCategoryBean{

    public String dishCategoryId;
    public String dishCategoryName;
    public List<DishBean> dishArray;
    
    public DishCategoryBean() {
        // TODO Auto-generated constructor stub
        this.dishCategoryId = "";
        this.dishCategoryName = "";
        this.dishArray = new ArrayList<DishBean>();
    }

}
