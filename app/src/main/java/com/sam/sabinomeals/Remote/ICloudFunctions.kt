package com.sam.sabinomeals.Remote

import com.sam.sabinomeals.models.BraintreeToken
import com.sam.sabinomeals.models.BraintreeTransaction
import io.reactivex.Observable
import retrofit2.http.*

interface ICloudFunctions {
    @GET("token")
    fun getToken(@HeaderMap headers: Map<String,String>): Observable<BraintreeToken>

    @POST("checkout")
    @FormUrlEncoded
    fun  submitPayement(@HeaderMap headers: Map<String,String>,
                        @Field("amount") amount:Double,
                        @Field("payment_method_nonce") nonce:String): Observable<BraintreeTransaction>
}