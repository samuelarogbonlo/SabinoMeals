package com.sam.sabinomeals.Callback

import com.sam.sabinomeals.models.PopularCategoryModel


interface IPopularCategoryCallback {
    fun onPopularLoadSuccess(popularCategoryModels: List<PopularCategoryModel>)
    fun onPopularLoadFailed(message:String)
}