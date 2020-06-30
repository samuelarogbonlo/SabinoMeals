package com.sam.sabinomeals.ui.view_order

import android.app.Dialog
import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sam.sabinomeals.Callback.IloadOrderCallBackListener
import com.sam.sabinomeals.Callback.MyButtonCallBack
import com.sam.sabinomeals.adapter.MyOrderAdapter
import com.sam.sabinomeals.commons.Common
import com.sam.sabinomeals.commons.MySwiperHelper.MyButton
import com.sam.sabinomeals.models.OrderModel
import dmax.dialog.SpotsDialog
import net.sam.sabinomeals.R
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ViewOrderFragment : Fragment(),
    IloadOrderCallBackListener {


    private lateinit var viewModel: ViewOrderViewModel
    var dialog: Dialog? = null
    var listener: IloadOrderCallBackListener? = null
    var recycler_order: RecyclerView? = null
    var adapter: MyOrderAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(ViewOrderViewModel::class.java)
        val root = inflater.inflate(R.layout.view_order_fragment, container, false)
        initView(root)
        loadOrderFromFirebase()
        viewModel.orderMutableList!!.observe(viewLifecycleOwner, Observer {
            Collections.reverse(it!!)
            adapter = MyOrderAdapter(
                requireContext(),
                it!! as MutableList<OrderModel>
            )
            recycler_order!!.adapter = adapter
        })
        return root
    }

    private fun initView(root: View?) {
        dialog = SpotsDialog.Builder().setCancelable(false).setContext(context).build();
        listener = this;
        recycler_order = root!!.findViewById(R.id.recycler_orders_view) as RecyclerView
        recycler_order!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context);
        recycler_order!!.layoutManager = layoutManager;
        recycler_order!!.addItemDecoration(
            DividerItemDecoration(
                context,
                layoutManager.orientation
            )
        )

        val swipe = object : Common.MySwiperHelper(requireContext(), recycler_order!!, 200) {
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {
                buffer.add(MyButton(context!!, "Cancel", 30, 0, Color.parseColor("#E13F3F"),
                        object : MyButtonCallBack {
                            override fun onClick(pos: Int) {
                                val orderModel =
                                    (recycler_order!!.adapter as MyOrderAdapter).getItemAtPosition(
                                        pos
                                    )
                                if (orderModel.status == 0) {
                                    val builder = AlertDialog.Builder(context!!)
                                        .setTitle("Order cancellation")
                                        .setMessage("Do you want to delete this Order?")
                                        .setNegativeButton("No") { dialog, which -> dialog.dismiss() }
                                        .setPositiveButton("Yes") { dialog, which ->
                                            val updateData = HashMap<String, Any>()
                                            updateData["status"] = -1
                                            FirebaseDatabase.getInstance()
                                                .getReference(Common.ORDER_REFERENCE)
                                                .child(orderModel.key!!)
                                                .updateChildren(updateData)
                                                .addOnFailureListener { exception: Exception ->
                                                    Toast.makeText(
                                                        context,
                                                        "" + exception.message,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                                .addOnSuccessListener { void: Void? ->
                                                    orderModel.status = -1
                                                    (recycler_order!!.adapter as MyOrderAdapter).setItemAtPosition(
                                                        pos,
                                                        orderModel
                                                    )
                                                    recycler_order!!.adapter!!.notifyItemChanged(pos)
                                                    Toast.makeText(
                                                        context,
                                                        "Successfully Cancelled Order",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                        }
                                }
                            }

                        }))

            }


        }
    }

    private fun loadOrderFromFirebase() {
        val orderModelList = ArrayList<OrderModel>()
        FirebaseDatabase.getInstance().getReference(Common.ORDER_REFERENCE)
            .orderByChild("userId")
            .equalTo(Common.currentUser!!.uid)
            .limitToLast(100)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    listener!!.onOrderLoadFailed(p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (orderSnapShot in p0.children) {
                        val orderModel = orderSnapShot.getValue(OrderModel::class.java)
                        orderModel!!.key = orderSnapShot.key
                        orderModelList.add(orderModel)
                    }
                    listener!!.onOrderLoadSuccess(orderModelList)
                }
            })
    }

    override fun onOrderLoadSuccess(orderModel: List<OrderModel>) {
        dialog!!.dismiss()
        viewModel.setOrderMutableList(orderModel)
    }

    override fun onOrderLoadFailed(message: String) {
        dialog!!.dismiss()
        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show()
    }


}
