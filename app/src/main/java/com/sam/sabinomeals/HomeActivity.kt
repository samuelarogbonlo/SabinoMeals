package com.sam.sabinomeals

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import co.paystack.android.PaystackSdk
import com.andremion.counterfab.CounterFab
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sam.sabinomeals.commons.Common
import com.sam.sabinomeals.database.CartDataSource
import com.sam.sabinomeals.database.CartDatabase
import com.sam.sabinomeals.database.LocalCartDataSource
import com.sam.sabinomeals.eventBus.*
import com.sam.sabinomeals.models.CategoryModel
import com.sam.sabinomeals.models.FoodModel
import dmax.dialog.SpotsDialog
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.sam.sabinomeals.R
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast


class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var navController: NavController? = null
    private var cartDataSource: CartDataSource? = null
    private var fab: CounterFab? = null
    private var drawerLayout: DrawerLayout? = null
    private var dialog: android.app.AlertDialog? = null
    private var menuItemClick=-1

    override fun onResume() {
        super.onResume()
        countCartItem()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        dialog = SpotsDialog.Builder().setContext(this).setCancelable(false).build()


        cartDataSource = LocalCartDataSource(CartDatabase.getInstance(this).cartDao())

        fab = findViewById<CounterFab>(R.id.fab)
        fab!!.setOnClickListener { view ->
            navController!!.navigate(R.id.nav_cart)
        }
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_category,
                R.id.nav_food_list,
                R.id.nav_food_detail,
                R.id.nav_cart,
                R.id.nav_view_logout,
                R.id.nav_view_orders
            ), drawerLayout
        )
        setupActionBarWithNavController(navController!!, appBarConfiguration)
        navView.setupWithNavController(navController!!)

        val headerView = navView.getHeaderView(0)
        val txt_user = headerView.findViewById<TextView>(R.id.txt_user)

        Common.setSpanString("Hi There!", Common.currentUser!!.name, txt_user)
        navView.setNavigationItemSelectedListener { item ->
            item.isChecked = true
            drawerLayout!!.closeDrawers()
            if (item.itemId == R.id.nav_view_logout) {
                signout()
            }
            else if (item.itemId == R.id.nav_home) {
                if(menuItemClick != item.itemId)
                     navController!!.navigate(R.id.nav_home)
            }
            else if (item.itemId == R.id.nav_category) {
                if(menuItemClick != item.itemId)
                     navController!!.navigate(R.id.nav_category)
            } else if (item.itemId == R.id.nav_cart) {
                if(menuItemClick != item.itemId)
                    navController!!.navigate(R.id.nav_cart)
            }else if (item.itemId == R.id.nav_view_orders) {
                if(menuItemClick != item.itemId)
                    navController!!.navigate(R.id.nav_view_orders)
            }

            menuItemClick =item.itemId
            true
        }


        countCartItem()

        PaystackSdk.initialize(applicationContext);
    }

    private fun signout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
            .setMessage("Do you really want to Sign Out?")
            .setNegativeButton("To Cancel") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("OK") { dialog, _ ->
               Common.FOOD_SELECTED = null
                Common.CATEGORY_SELECTED = null
               Common.currentUser = null
                FirebaseAuth.getInstance().signOut()

                val intent = Intent(this@HomeActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }

        val dialog = builder.create()
        dialog.show()

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onCategorySelect(event: CategoryClick) {
        if (event.isSuccess)
        {
            //Toast.makeText(this, "Click to"+event.category.name,Toast.LENGTH_SHORT).show()
            navController!!.navigate(R.id.nav_food_list)
            // toast(event.get.name!!)
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onFoodSelect(event: FoodClick) {
        if (event.isSuccess)
        {
            navController!!.navigate(R.id.nav_food_detail)
            // toast(event.get.name!!)
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onCounterCartEvent(event: CounterCartEvent) {
        if (event.isSuccess)
        {
            countCartItem()
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onHiddenCartEvent(event: HideFabCart) {
        if (event.hidden)
        {
            fab!!.hide()
        } else {
            fab!!.show()
        }
    }

    private fun countCartItem() {
        cartDataSource!!.countItemInCart(Common.currentUser!!.uid!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Int> {
                override fun onSuccess(t: Int) {
                    fab!!.count = t
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    if (!e.message!!.contains("Query returned empty")) {
                        toast("[Count Basket] " + e.message)
                    } else {
                        fab!!.count = 0
                    }
                }

            })
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onPopularCategoryEvent(event: PopularCategoryClick) {
        if (event.popularCategory != null) {
            dialog!!.show()

            FirebaseDatabase.getInstance().getReference(Common.CATEGORY_REFERENCE)
                .child(event.popularCategory.menu_id!!)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        dialog!!.dismiss()
                        Toast.makeText(this@HomeActivity, "" + p0.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            Common.CATEGORY_SELECTED = p0.getValue(
                                CategoryModel::class.java)
                            Common.CATEGORY_SELECTED!!.menu_id = p0.key
                            //load food
                            FirebaseDatabase.getInstance().getReference(Common.CATEGORY_REFERENCE)
                                .child(event.popularCategory.menu_id!!)
                                .child(Common.FOOD_REFERENCE)
                                .orderByChild("id")
                                .equalTo(event.popularCategory.food_id)
                                .limitToLast(1)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {
                                        dialog!!.dismiss();
                                        Toast.makeText(
                                            this@HomeActivity,
                                            "" + p0.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    override fun onDataChange(p0: DataSnapshot) {
                                        if (p0.exists()) {
                                            for (itemSnapShot in p0.children) {
                                                Common.FOOD_SELECTED =
                                                    itemSnapShot.getValue(FoodModel::class.java)
                                                Common.FOOD_SELECTED!!.key = itemSnapShot.key
                                            }
                                            navController!!.navigate(R.id.nav_food_detail)

                                        } else {
                                            dialog!!.dismiss()
                                            Toast.makeText(
                                                this@HomeActivity,
                                                "Requested Meal does not exist",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        dialog!!.dismiss()
                                    }

                                })
                        }
                    }

                })
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    fun onBestDealItemClick(event:BestDealItemClick){
        if(event.bestDealModel != null){
            dialog!!.show();
            FirebaseDatabase.getInstance().getReference(Common.CATEGORY_REFERENCE)
                .child(event.bestDealModel!!.menu_id!!)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        dialog!!.dismiss()
                        Toast.makeText(this@HomeActivity, "" + p0.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            Common.CATEGORY_SELECTED = p0.getValue(CategoryModel::class.java)
                            Common.CATEGORY_SELECTED!!.menu_id = p0.key
                            //load food
                            FirebaseDatabase.getInstance().getReference(Common.CATEGORY_REFERENCE)
                                .child(event.bestDealModel!!.menu_id!!)
                                .child(Common.FOOD_REFERENCE)
                                .orderByChild("id")
                                .equalTo(event.bestDealModel!!.food_id)
                                .limitToLast(1)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {
                                        dialog!!.dismiss();
                                        Toast.makeText(
                                            this@HomeActivity,
                                            "" + p0.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    override fun onDataChange(p0: DataSnapshot) {
                                        if (p0.exists()) {
                                            for (itemSnapShot in p0.children) {
                                                Common.FOOD_SELECTED =
                                                    itemSnapShot.getValue(FoodModel::class.java)
                                                Common.FOOD_SELECTED!!.key = itemSnapShot.key
                                            }
                                            navController!!.navigate(R.id.nav_food_detail)

                                        } else {
                                            dialog!!.dismiss()
                                            Toast.makeText(
                                                this@HomeActivity,
                                                "Requested Meal does not exist",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        dialog!!.dismiss()
                                    }

                                })
                        }
                    }

                })
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    fun onMenuItemBack(event:MenuItemBack){
        menuItemClick =1
        if(supportFragmentManager.backStackEntryCount>0)
            supportFragmentManager.popBackStack()
    }
}
