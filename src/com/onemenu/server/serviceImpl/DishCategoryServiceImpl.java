package com.onemenu.server.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.onemenu.server.dao.DishCategoryDAO;
import com.onemenu.server.dao.DishDAO;
import com.onemenu.server.javabean.DishBean;
import com.onemenu.server.javabean.DishCategoryBean;
import com.onemenu.server.javabean.NDishCategoryBean;
import com.onemenu.server.javabean.NMenuItemDishBean;
import com.onemenu.server.model.Dish;
import com.onemenu.server.model.DishCategory;
import com.onemenu.server.service.DishCategoryService;
import com.onemenu.server.util.AwsS3Utils;

public class DishCategoryServiceImpl extends AbstractServiceImpl implements DishCategoryService {

    private DishDAO mDishDAO;
    private DishCategoryDAO mDishCategoryDAO;

    @Autowired
    public void setmDishDAO(DishDAO mDishDAO) {
        this.mDishDAO = mDishDAO;
    }

    @Autowired
    public void setmDishCategoryDAO(DishCategoryDAO mDishCategoryDAO) {
        this.mDishCategoryDAO = mDishCategoryDAO;
    }

    @Override
    public List<DishCategoryBean> getDishCategoryListByRestaurantId(long restaurantId) {

        List<DishCategory> dishCategoryList =
                mDishCategoryDAO.getDishCategoryListByRestaurantId(restaurantId);

        List<DishCategoryBean> dishCategoryDetailBeanList = new ArrayList<DishCategoryBean>();

        for (DishCategory dishCategory : dishCategoryList) {

            DishCategoryBean dishCategoryBean = new DishCategoryBean();
            
            if (dishCategory != null) {

                dishCategoryBean.dishCategoryId = String.valueOf(dishCategory.getmId());
                dishCategoryBean.dishCategoryName = dishCategory.getmName();
                
                for (Dish dish : dishCategory.getmDishSet()) {
                    
                    DishBean dishBean = new DishBean();

                    if (dish != null) {

                        dishBean.dishId = String.valueOf(dish.getmId());
                        dishBean.dishPrice = String.valueOf(dish.getmPrice());
                        dishBean.dishName = dish.getmName();
                        dishBean.dishImageUrl =
                                AwsS3Utils.getAwsS3RestaurantResUrl()
                                        + dish.getmDishCategory().getmRestaurant().getmFolderName() + "/"
                                        + dish.getmImageName();
                    }

                    dishCategoryBean.dishArray.add(dishBean);
                }
            }
            
            if(dishCategoryBean.dishArray.size() != 0) {
                dishCategoryDetailBeanList.add(dishCategoryBean);
            }
        }

        return dishCategoryDetailBeanList;
    }

	@Override
	public List<NDishCategoryBean> getDishCategorysByRestaurantId(long restaurantId) {
		List<DishCategory> dishCategoryList =  mDishCategoryDAO.getDishCategoryListByRestaurantId(restaurantId);

        List<NDishCategoryBean> dishCategoryDetailBeanList = new ArrayList<NDishCategoryBean>();

        for (DishCategory dishCategory : dishCategoryList) {

        	NDishCategoryBean dishCategoryBean = new NDishCategoryBean();
            
            if (dishCategory != null) {

                dishCategoryBean.dishCategoryName = dishCategory.getmName();
                
                dishCategoryBean.dishArray = new ArrayList<NMenuItemDishBean>();
                
                for (Dish dish : dishCategory.getmDishSet()) {
                    
                	NMenuItemDishBean dishBean = new NMenuItemDishBean();

                    if (dish != null) {
                        dishBean.dishId = String.valueOf(dish.getmId());
                        dishBean.dishPrice = String.valueOf(dish.getmPrice());
                        dishBean.dishName = dish.getmName();
                        dishBean.dishImageUrl =
                                AwsS3Utils.getAwsS3RestaurantResUrl()
                                        + dish.getmDishCategory().getmRestaurant().getmFolderName() + "/"
                                        + dish.getmImageName();
                    }

                    dishCategoryBean.dishArray.add(dishBean);
                }
            }
            
            if(dishCategoryBean.dishArray.size() != 0) {
                dishCategoryDetailBeanList.add(dishCategoryBean);
            }
        }

        return dishCategoryDetailBeanList;
	}
}
