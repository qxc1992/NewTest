package com.newtest

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by yupenglei on 2018/5/8.
 */
class MyAdapter : RecyclerView.Adapter<MyAdapter.MyVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_test, parent, false)
        return MyVH(view)
    }

    override fun getItemCount(): Int = 20

    override fun onBindViewHolder(holder: MyVH, position: Int) {
        holder.setData(position)
    }


    class MyVH(item: View) : RecyclerView.ViewHolder(item) {

        fun setData(position: Int) {
            (itemView as TextView).text = "$position 大大"
        }
    }
}