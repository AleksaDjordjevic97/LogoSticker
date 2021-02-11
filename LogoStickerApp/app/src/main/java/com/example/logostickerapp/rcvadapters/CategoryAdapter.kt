package com.example.logostickerapp.rcvadapters

import android.content.Context
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.logostickerapp.R

class CategoryAdapter(private val context: Context,
                      private val iconResArray: Int,
                      private val listener:OnCategoryClickListener,
                      private val mode:String): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>()
{

    interface OnCategoryClickListener
    {
        fun onCategoryClick(position: Int, mode: String)
    }

    inner class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val btnCategory: ImageButton = itemView.findViewById(R.id.btnCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_category,parent,false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int)
    {
        val categoryIconsArray: TypedArray = context.resources.obtainTypedArray(iconResArray)
        holder.btnCategory.setImageResource(categoryIconsArray.getResourceId(position,0))
        holder.btnCategory.setOnClickListener{ listener.onCategoryClick(position, mode) }
    }

    override fun getItemCount(): Int
    {
        return  context.resources.obtainTypedArray(iconResArray).length()
    }


}