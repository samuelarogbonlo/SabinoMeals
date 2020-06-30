package com.sam.sabinomeals.Callback

import com.sam.sabinomeals.models.CategoryModel


interface ICategoryCallBackListener {
    fun onCategoryLoadSuccess(categoryModels: List<CategoryModel>)
    fun onCategoryLoadFailed(message:String)
}