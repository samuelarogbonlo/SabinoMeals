package com.sam.sabinomeals.Callback

import com.sam.sabinomeals.models.BestDealModel


interface IBestDealsCallbackListener {
    fun onBestDealsLoadSuccess(bestDeals: List<BestDealModel>);
    fun onBestDealsLoadFailed(message:String);
}