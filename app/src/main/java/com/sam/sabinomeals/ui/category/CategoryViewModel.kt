package com.sam.sabinomeals.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sam.sabinomeals.Callback.ICategoryCallBackListener
import com.sam.sabinomeals.commons.Common
import com.sam.sabinomeals.models.CategoryModel
import javax.security.auth.callback.Callback

class CategoryViewModel : ViewModel(), ICategoryCallBackListener {
    private  var categoryMutableList:MutableLiveData<List<CategoryModel>>?=null
    private lateinit var messageError:MutableLiveData<String>
    private var categoryCallbackListener: ICategoryCallBackListener?=null

    val categoryList:LiveData<List<CategoryModel>>
        get() {
            if(categoryMutableList==null)
            {
                categoryMutableList= MutableLiveData()
                messageError= MutableLiveData()
                loadCategory()
            }
            return categoryMutableList!!
        }

    private fun loadCategory() {
        val tempdir = ArrayList<CategoryModel>()
        val categoryReference = FirebaseDatabase.getInstance().getReference(Common.CATEGORY_REFERENCE)
        categoryReference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                categoryCallbackListener?.onCategoryLoadFailed(p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
               for (itemSnapshot in p0.children)
               {
                   val model = itemSnapshot.getValue(CategoryModel::class.java)
                   model?.menu_id=itemSnapshot.key
                   tempdir.add(model!!)
               }
                categoryCallbackListener?.onCategoryLoadSuccess(tempdir)
            }

        })
    }

    init {
        categoryCallbackListener =this
    }

    override fun onCategoryLoadSuccess(categoryModels: List<CategoryModel>) {
        categoryMutableList!!.value =categoryModels
    }

    override fun onCategoryLoadFailed(message: String) {
        messageError.value=message
    }
}