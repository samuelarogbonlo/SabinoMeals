package com.sam.sabinomeals.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sam.sabinomeals.Callback.IBestDealsCallbackListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sam.sabinomeals.Callback.IPopularCategoryCallback
import com.sam.sabinomeals.commons.Common
import com.sam.sabinomeals.models.BestDealModel
import com.sam.sabinomeals.models.PopularCategoryModel


abstract class HomeViewModel : ViewModel(), IPopularCategoryCallback, IBestDealsCallbackListener {
    //_root_ide_package_.com.sam.sabinomeals.callBacks.IPopularCategoryCallbackListener,
    //_root_ide_package_.com.sam.sabinomeals.Callback.IBestDealsCallbackListener

    override fun onBestDealsLoadSuccess(bestDeals: List<BestDealModel>) {
        bestDealLiveData!!.value = bestDeals
    }

    override fun onBestDealsLoadFailed(message: String) {
        messageError.value = message
    }

    override fun onPopularLoadSuccess (popularModelList: List<PopularCategoryModel>) {
        popularMutableList!!.value = popularModelList
    }

    override fun onPopularLoadFailed(message: String) {
        messageError.value = message
    }

    private  var popularMutableList:MutableLiveData<List<PopularCategoryModel>>?=null
    private var bestDealLiveData:MutableLiveData<List<BestDealModel>>?=null
    private lateinit var messageError:MutableLiveData<String>
    private  var popularCategoryCallbackListener: IPopularCategoryCallback?=null
    private var bestBestDealsCallbackListener: IBestDealsCallbackListener?=null

    //private var bestDealLiveData:MutableLiveData<List<BestDealModel>>?=null


    val popularList:LiveData<List<PopularCategoryModel>>
        get() {
            if(popularMutableList == null)
            {
                popularMutableList = MutableLiveData()
                messageError= MutableLiveData()
                loadPopularList()
            }
            return popularMutableList!!
        }
   val bestDealList:LiveData<List<BestDealModel>>
    get() {
        if(bestDealLiveData == null){
           bestDealLiveData = MutableLiveData()
           messageError = MutableLiveData()
           loadBeastDeal()
        }
       return  bestDealLiveData!!
    }

    private fun loadBeastDeal() {
        val tempdir =ArrayList<BestDealModel>()
        val bestDealReference = FirebaseDatabase.getInstance().getReference(Common.BEST_DEAL_REFERENCE)
        bestDealReference.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                bestBestDealsCallbackListener?.onBestDealsLoadFailed(p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
               for (itemSnapshot in p0!!.children)
               {
                   val model = itemSnapshot.getValue(BestDealModel::class.java)
                   tempdir.add(model!!)
               }
                bestBestDealsCallbackListener?.onBestDealsLoadSuccess(tempdir)
            }

        })
    }


    private fun loadPopularList() {
        val tempdir =ArrayList<PopularCategoryModel>()
        val popularReference = FirebaseDatabase.getInstance().getReference(Common.POPULAR_CATEGORY_REFERENCE)
        popularReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                popularCategoryCallbackListener?.onPopularLoadFailed(p0.message!!)
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (itemSnapshot in p0!!.children)
                {
                    val model = itemSnapshot.getValue(PopularCategoryModel::class.java)
                    tempdir.add(model!!)
                }
                popularCategoryCallbackListener?.onPopularLoadSuccess(tempdir)
            }
        })
    }

    init {
        popularCategoryCallbackListener =this
        bestBestDealsCallbackListener =this
    }



}