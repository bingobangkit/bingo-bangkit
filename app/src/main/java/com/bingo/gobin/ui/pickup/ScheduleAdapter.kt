package com.bingo.gobin.ui.pickup

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bingo.gobin.data.model.Order
import com.bingo.gobin.databinding.RowScheduleBinding

class ScheduleAdapter(private val list: List<Order>) : RecyclerView.Adapter<ScheduleAdapter.ListViewHolder>(){
    class ListViewHolder(val binding: RowScheduleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(RowScheduleBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
       val data = list[position]
        with(holder.binding){
            txtDateRecycle.text = data.date
            txtKgRecycle.text =data.amount+"kg"
        }
    }

    override fun getItemCount(): Int = list.size
}