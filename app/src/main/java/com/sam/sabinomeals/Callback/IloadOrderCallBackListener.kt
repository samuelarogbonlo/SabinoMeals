package com.sam.sabinomeals.Callback

import com.sam.sabinomeals.models.OrderModel


interface IloadOrderCallBackListener {
    fun onOrderLoadSuccess(orderModel: List<OrderModel>)
    fun onOrderLoadFailed(message:String)
}