package com.sam.sabinomeals.ui.food

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sam.sabinomeals.adapter.MyFoodAdapter
import com.sam.sabinomeals.commons.Common
import com.sam.sabinomeals.eventBus.MenuItemBack
import dmax.dialog.SpotsDialog
import net.sam.sabinomeals.R
import org.greenrobot.eventbus.EventBus

class FoodFragment : Fragment() {

    private lateinit var foodViewModel: FoodViewModel
    private var recycler_food_list:RecyclerView?=null
    private lateinit var dialog: AlertDialog
    private var layoutAnimationController:LayoutAnimationController?=null
    private var adapter: MyFoodAdapter?=null


    override fun onStop() {

        if(adapter != null)
            adapter!!.onStop()
        super.onStop()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_food, container, false)
        initView(root)

        foodViewModel.foodList.observe(viewLifecycleOwner, Observer {
            val listData = it
             adapter= MyFoodAdapter(
                 requireContext(),
                 listData
             )
            recycler_food_list?.adapter = adapter
        })
        return root
    }

    private fun initView(root: View?) {
        recycler_food_list = root?.findViewById(R.id.recycler_food_list) as RecyclerView
        layoutAnimationController = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_item_from_left)
        dialog= SpotsDialog.Builder().setContext(context).setCancelable(false).setMessage("Download .....").build()
        recycler_food_list?.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)

        (activity as AppCompatActivity).supportActionBar!!.title = Common.CATEGORY_SELECTED?.name

    }

    override fun onDestroy() {
        EventBus.getDefault().postSticky(MenuItemBack())
        super.onDestroy()
    }
}
