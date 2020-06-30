package com.sam.sabinomeals.ui.comment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sam.sabinomeals.models.CommentModel

class CommentViewModel : ViewModel() {

    private  var commentMutable: MutableLiveData<List<CommentModel>>?=null

    init {
        commentMutable = MutableLiveData()
    }

    fun setCommentModel(commentModels: List<CommentModel>) {
        if(commentMutable !=null )
            commentMutable?.value =commentModels
    }

    fun getMutableLiveDataComment():MutableLiveData<List<CommentModel>>{
        if(commentMutable==null)
            commentMutable = MutableLiveData()
        return commentMutable!!
    }
}
