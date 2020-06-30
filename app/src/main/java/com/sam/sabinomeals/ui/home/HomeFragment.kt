package com.sam.sabinomeals.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asksira.loopingviewpager.LoopingViewPager
import com.sam.sabinomeals.adapter.BestDealAdapter
import com.sam.sabinomeals.adapter.MyPopularCategoriesAdapter
import com.sam.sabinomeals.eventBus.MenuItemBack
import kotlinx.android.synthetic.main.fragment_home.*
import net.sam.sabinomeals.R
import net.sam.sabinomeals.R.id.recycler_popular
import org.greenrobot.eventbus.EventBus

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    //@BindView(R.id.recycler_popular)

    var recyclerView:RecyclerView?=null

    var viewPager:LoopingViewPager?=null
    var layoutAnimationController:LayoutAnimationController?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        //unbinder = ButterKnife.bind(this, root)
        initView(root)

        homeViewModel.popularList.observe(viewLifecycleOwner, Observer {
            val listData = it
            val adapter = MyPopularCategoriesAdapter(requireContext(), listData)
            recyclerView!!.adapter = adapter
            recyclerView!!.layoutAnimation = layoutAnimationController
        })
        homeViewModel.bestDealList.observe(viewLifecycleOwner, Observer {
            val listData = it
            val adapter = BestDealAdapter(requireContext()!!, listData, true)
            viewPager!!.adapter = adapter
        })

        return root
    }

    private fun initView(root:View) {
        layoutAnimationController = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_item_from_left)
        recyclerView = root.findViewById(R.id.recycler_popular) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
        viewPager= root.findViewById(R.id.viewpager) as LoopingViewPager


    }

    override fun onResume() {
        super.onResume()
        viewPager!!.resumeAutoScroll()
    }

    override fun onPause() {
        viewPager!!.pauseAutoScroll()
        super.onPause()
    }

   // override fun onDestroy() {
    //    EventBus.getDefault().postSticky(MenuItemBack())
   //     super.onDestroy()
    //}
}
