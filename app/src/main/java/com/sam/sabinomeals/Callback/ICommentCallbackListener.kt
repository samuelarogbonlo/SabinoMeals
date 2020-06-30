package com.sam.sabinomeals.Callback

import com.sam.sabinomeals.models.CommentModel


interface ICommentCallbackListener {
    fun onCommentLoadSuccess(commentModels:List<CommentModel>)
    fun onCommentLoadFailed(message:String)
}