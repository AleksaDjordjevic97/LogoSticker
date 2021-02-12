package com.example.logostickerapp.rcvadapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.logostickerapp.R

class CategoryAdapter(private val context: Context,
                      private val listener:OnCategoryClickListener,
                      private val mode:String): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>()
{

    private val categories:ArrayList<CategoryViewHolder> = ArrayList()

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
        categories.add(holder)
        val categoryIconsArray = if(mode == "Bgd") context.resources.obtainTypedArray(R.array.bgd_categories) else context.resources.obtainTypedArray(R.array.logo_categories)
        holder.btnCategory.setImageResource(categoryIconsArray.getResourceId(position,0))
        holder.btnCategory.setOnClickListener{ listener.onCategoryClick(position, mode) }
        categoryIconsArray.recycle()
    }

    override fun getItemCount(): Int
    {
        val categoryIconsArray = if(mode == "Bgd") context.resources.obtainTypedArray(R.array.bgd_categories) else context.resources.obtainTypedArray(R.array.logo_categories)
        val itemCount = categoryIconsArray.length()
        categoryIconsArray.recycle()

        return itemCount
    }

    fun setActiveCategory(position:Int, mode:String)
    {
        val categoryIconsArray = if(mode == "Bgd") context.resources.obtainTypedArray(R.array.bgd_categories) else context.resources.obtainTypedArray(R.array.logo_categories)
        val categoryActiveIconsArray = if(mode == "Bgd") context.resources.obtainTypedArray(R.array.bgd_categories_active) else context.resources.obtainTypedArray(R.array.logo_categories_active)

        for(i in 0 until categories.size)
        {
            if(i != position)
                categories[i].btnCategory.setImageResource(categoryIconsArray.getResourceId(i,0))
            else
                categories[i].btnCategory.setImageResource(categoryActiveIconsArray.getResourceId(i,0))
        }

        categoryIconsArray.recycle()
        categoryActiveIconsArray.recycle()
    }


}