package com.sam.sabinomeals.commons

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.sam.sabinomeals.models.*
import net.sam.sabinomeals.R
import java.lang.Exception
import java.text.NumberFormat
import java.util.*

object Common {
    open class MySwiperHelper(requireContext: Context, recyclerOrder: RecyclerView, i: Int) {

        open fun instantiateMyButton(
            viewHolder: RecyclerView.ViewHolder,
            buffer: MutableList<com.sam.sabinomeals.commons.MySwiperHelper.MyButton>
        ) {
        }
    }

    val NOTI_CONTENT: String? = "content"
    var NOTI_TITLE: String? = "title"
    const val TOKEN_REFERENCE: String = "Tokens"
    var authorizeToken: String? = null
    var currentToken: String = ""
    const val ORDER_REFERENCE: String = "Order"
    val FOOD_REFERENCE: String = "foods"
    const val COMMENT_REFERENCE: String = "Comments"
    var FOOD_SELECTED: FoodModel? = null
    var CATEGORY_SELECTED: CategoryModel? = null
    val FULL_WIDHT_COLUMN: Int = 1
    val DEFAULT_COLUMN_COUNT: Int = 0
    const val CATEGORY_REFERENCE: String = "Category"
    const val BEST_DEAL_REFERENCE: String = "BestDeals"
    const val POPULAR_CATEGORY_REFERENCE: String = "MostPopular"
    const val USER_REFERENCE: String = "Users"
    var currentUser: UserModel? = null

    fun formatPrice(displayPrice: Double): String {
        if (displayPrice != 0.toDouble()) {
            val currency = Currency.getInstance("NGN")
            val numberFormat = NumberFormat.getCurrencyInstance()
            numberFormat.currency = currency

            return numberFormat.format(displayPrice)
        } else
            return "0 NGN"
    }

    fun reverformat(price: String): Double? {
        val currency = Currency.getInstance("NGN")
        val numberFormat = NumberFormat.getCurrencyInstance()

        numberFormat.currency = currency
        var priceForm = 0.toDouble()

        priceForm = numberFormat.parse(price).toDouble()

        return priceForm
    }

    fun calculateExtraPrice(
        userSelectedAddon: MutableList<AddonModel>?,
        userSelectedSize: SizesModel?
    ): Double {
        var result = 0.toDouble()

        if (userSelectedSize == null && userSelectedAddon == null)
            return 0.toDouble()
        else if (userSelectedSize == null) {
            for (addon in userSelectedAddon!!)
                result += addon.price!!.toDouble()
            return result
        } else if (userSelectedAddon == null) {
            return userSelectedSize.price!!.toDouble()
        } else {
            result = userSelectedSize.price!!.toDouble()
            for (addon in userSelectedAddon)
                result += addon.price!!.toDouble()
            return result
        }

    }

    fun setSpanString(txt: String, name: String?, txtView: TextView?) {
        val builder = SpannableStringBuilder()
        builder.append(txt)
        val txtSpannable = SpannableString(name)
        val boldSpan = StyleSpan(Typeface.BOLD)
        txtSpannable.setSpan(boldSpan, 0, name!!.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.append(txtSpannable)
        txtView!!.setText(builder, TextView.BufferType.SPANNABLE)
    }

    fun createOrderNumer(): String {
        return StringBuilder()
            .append(System.currentTimeMillis())
            .append(Math.abs(Random().nextInt()))
            .toString()
    }

    fun buildToken(authorizeToken: String?): String {
        return StringBuilder("Bearer").append(" ").append(authorizeToken).toString()
    }

    @SuppressLint("ShowToast")
    fun updateToken(context: Context, newToken: String) {
        FirebaseDatabase.getInstance()
            .getReference(TOKEN_REFERENCE!!)
            .child(currentUser!!.uid!!)
            .setValue(TokenModel(currentUser!!.phone, newToken))
            .addOnFailureListener { exception: Exception ->
                Toast.makeText(
                    context,
                    "" + exception.message,
                    Toast.LENGTH_SHORT
                )
            }
    }

    fun showNotification(
        context: Context,
        id: Int,
        title: String?,
        content: String?,
        intent: Intent?
    ) {
        var pendingIntent: PendingIntent? = null

        if (intent != null) {
            pendingIntent =
                PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val NOTIFICATION_CHANNEL_ID = "SabinoMeals"
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "SabinoMeals",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationChannel.description = "SabinoMeals"
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
                notificationManager.createNotificationChannel(notificationChannel)
            }

            val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.drawable.ic_restaurant_menu_black_24dp
                    )
                )

            if (pendingIntent != null) {
                builder.setContentIntent(pendingIntent)

                val notification = builder.build()
                notificationManager.notify(id, notification)

            }
        }
    }

    fun createTopicOrder(): String? {
        return StringBuilder("/topics/new_order").toString()
    }

    fun getDaystoWeek(i: Int): String {
        when(i){
           1->return  "Monday"
           2->return  "Tuesday"
           3->return  "Wednesday"
           4->return  "Thursday"
           5->return  "Friday"
           6->return  "Saturday"
           7->return  "Sunday"
        }
        return "UnKnowDay"
    }

    fun convertStatusToText(status: Int?): String{
        when(status){
            0->return  "Passed order"
            1->return  "Order in progress Delivery"
            2->return  "Delivered Order"
            3->return  "Cancelled order"
        }
        return "UnKnowStatus"
    }

}