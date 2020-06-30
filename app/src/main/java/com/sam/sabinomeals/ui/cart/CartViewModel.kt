package com.sam.sabinomeals.ui.cart

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sam.sabinomeals.commons.Common
import com.sam.sabinomeals.database.CartDataSource
import com.sam.sabinomeals.database.CartDatabase
import com.sam.sabinomeals.database.CartItem
import com.sam.sabinomeals.database.LocalCartDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class CartViewModel : ViewModel() {
    private  var mutableLiveDataCartItems: MutableLiveData<List<CartItem>>?=null
    private lateinit var messageError:MutableLiveData<String>
    private var cartDataSource: CartDataSource?=null
    private var compositeDisposable:CompositeDisposable = CompositeDisposable()

    fun initCartDataSource(context: Context){
        cartDataSource = LocalCartDataSource(CartDatabase.getInstance(context).cartDao())
    }

    fun getMutableLiveDataCartItems():MutableLiveData<List<CartItem>>{
        if(mutableLiveDataCartItems == null)
            mutableLiveDataCartItems =  MutableLiveData()
        getCartItems()
        return mutableLiveDataCartItems!!
    }


    private fun getCartItems(){
        compositeDisposable.addAll(cartDataSource!!.getAllCart(Common.currentUser!!.uid!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({cartItems ->
                mutableLiveDataCartItems!!.value = cartItems
            },{
                t: Throwable? -> mutableLiveDataCartItems!!.value=null
            })
        )
    }
fun onStop(){
    compositeDisposable.clear()
}
}
