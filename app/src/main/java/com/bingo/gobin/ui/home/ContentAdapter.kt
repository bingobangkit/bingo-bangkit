package com.bingo.gobin.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.bingo.gobin.data.content.CommonUses
import com.bingo.gobin.databinding.GridCommonUsesBinding

class ContentAdapter(var list: List<CommonUses> = emptyList()) :
    RecyclerView.Adapter<ContentAdapter.GridViewHolder>() {



    inner class GridViewHolder(val binding: GridCommonUsesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder =
        GridViewHolder(
            GridCommonUsesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val data = list[position]
        holder.binding.apply {
            imageCommon.load(data.image){
                transformations(RoundedCornersTransformation(16f))
            }
            nameCommon.text = data.name
        }

    }

    override fun getItemCount(): Int = list.size



}