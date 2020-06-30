package com.sam.sabinomeals.ui.view_order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sam.sabinomeals.models.OrderModel


class ViewOrderViewModel : ViewModel() {
      var orderMutableList: MutableLiveData<List<OrderModel>>? =null

    init {
        orderMutableList = MutableLiveData()
    }

    fun setOrderMutableList(orderModelList:List<OrderModel>){
        orderMutableList!!.value = orderModelList
    }
}
