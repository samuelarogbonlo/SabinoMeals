package com.sam.sabinomeals.ui.category

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sam.sabinomeals.adapter.MyCategoryAdapter
import com.sam.sabinomeals.commons.Common
import com.sam.sabinomeals.commons.SpacesItemDecoration
import com.sam.sabinomeals.eventBus.MenuItemBack
import dmax.dialog.SpotsDialog
import net.sam.sabinomeals.R
import org.greenrobot.eventbus.EventBus


class CategoryFragment : Fragment() {

    private lateinit var categoryViewModel: CategoryViewModel
    private var recycler_menu :RecyclerView?=null
    private var layoutAnimationController:LayoutAnimationController?=null
    private lateinit var dialog:AlertDialog
    private var adapter: MyCategoryAdapter?=null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        categoryViewModel =
            ViewModelProviders.of(this).get(CategoryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_category, container, false)
        initView(root)



        categoryViewModel.categoryList.observe(viewLifecycleOwner, Observer {
                dialog.dismiss()
                val listData=it
                 adapter = MyCategoryAdapter(requireContext(), listData)
                recycler_menu!!.adapter = adapter
                recycler_menu!!.layoutAnimation  =layoutAnimationController
        })

        return root
    }

    private fun initView(root: View?) {
        recycler_menu = root?.findViewById(R.id.recycler_menu) as RecyclerView
        layoutAnimationController = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_item_from_left)
        recycler_menu!!.setHasFixedSize(true)
        dialog= SpotsDialog.Builder().setContext(context).setCancelable(false).setMessage("Downloading .....").build()
        dialog.show()
        val layoutManager = GridLayoutManager(context,2)
        layoutManager.orientation=RecyclerView.VERTICAL
        layoutManager.spanSizeLookup = object :GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                return if(adapter !=null)
                {
                    when(adapter!!.getItemViewType(position)){
                        Common.DEFAULT_COLUMN_COUNT -> 1
                        Common.FULL_WIDHT_COLUMN->2
                        else -> -1
                    }
                }else
                    -1
            }

        }
        recycler_menu!!.layoutManager = layoutManager
        recycler_menu!!.addItemDecoration(SpacesItemDecoration(8))
    }

    override fun onDestroy() {
        EventBus.getDefault().postSticky(MenuItemBack())
        super.onDestroy()
    }
}
