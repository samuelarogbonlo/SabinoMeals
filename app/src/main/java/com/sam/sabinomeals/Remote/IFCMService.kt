package com.sam.sabinomeals.Remote

import com.sam.sabinomeals.models.FCMRespose
import com.sam.sabinomeals.models.FCMSendData
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface IFCMService {
    @Headers(
        "Content-Type:application/json",
        "Authorization:key=AAAA4pujo5A:APA91bFa950f9Otszn33P_9Qdi8cU5K_blRIJEycs3fYrwDYGONWi4kjbux721DhmM8ht8EJRMjF7knVRpF2wBr2-BQ2rBHbCCu_FwPgp33kD1lMal2QN-xj-4ZuxbOipJeAKk0PWCaN"
    )
    @POST("fcm/send")
    fun  sendNotification(@Body body: FCMSendData):  Observable<FCMRespose>
}