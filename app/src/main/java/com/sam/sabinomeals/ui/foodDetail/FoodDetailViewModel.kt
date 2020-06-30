package com.sam.sabinomeals.ui.foodDetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sam.sabinomeals.commons.Common
import com.sam.sabinomeals.models.CommentModel
import com.sam.sabinomeals.models.FoodModel


class FoodDetailViewModel : ViewModel() {


    private  var foodMutable: MutableLiveData<FoodModel>?=null
    private  var commentMutable: MutableLiveData<CommentModel>?=null

    init {
        commentMutable = MutableLiveData()
    }


    fun getMutableLiveDataFood():MutableLiveData<FoodModel>{
        if(foodMutable==null)
            foodMutable= MutableLiveData()
        foodMutable?.value= Common.FOOD_SELECTED
        return foodMutable!!
    }

    fun setFoodModel(foodModel: FoodModel){
        if(foodMutable != null)
            foodMutable?.value = foodModel
    }

    fun setCommentModel(commentModel: CommentModel) {
        if(commentMutable !=null )
            commentMutable?.value =commentModel
    }

    fun getMutableLiveDataComment():MutableLiveData<CommentModel>{
        if(commentMutable==null)
            commentMutable = MutableLiveData()
        return commentMutable!!
    }


}
