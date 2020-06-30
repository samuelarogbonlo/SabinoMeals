package com.sam.sabinomeals.ui.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sam.sabinomeals.commons.Common
import com.sam.sabinomeals.models.FoodModel


class FoodViewModel : ViewModel() {
    private  var foodMutableList:MutableLiveData<List<FoodModel>>?=null

    val foodList:LiveData<List<FoodModel>>
        get() {
            if(foodMutableList == null){
                foodMutableList = MutableLiveData()
            }
            foodMutableList!!.value = Common.CATEGORY_SELECTED?.foods
            return foodMutableList!!
        }
}