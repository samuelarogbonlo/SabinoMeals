package com.sam.sabinomeals.Callback

import com.sam.sabinomeals.models.OrderModel

interface ILoadTimeFromFireBaseListener {
    fun onLoadTimeSuccess(orderModel: OrderModel, estimateTimeInMs:Long )
    fun onLoadTimeFailed(message:String)

}