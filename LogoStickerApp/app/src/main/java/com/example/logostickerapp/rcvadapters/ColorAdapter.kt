package com.example.logostickerapp.rcvadapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.logostickerapp.R

class ColorAdapter(private val context: Context?,
                   private val mode: String,
                   private val listener: OnColorClickListener) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>()
{
    interface OnColorClickListener
    {
        fun onColorClick(colorImgResource:Int)
    }

    inner class ColorViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val colorCell: ImageView = itemView.findViewById(R.id.cell_color_pattern)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder
    {
        val view = LayoutInflater.from(context).inflate(R.layout.view_holder_color,parent,false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int)
    {
        if(context != null)
        {
            val arrayOfColors =
                if (mode == "Color") context.resources.obtainTypedArray(R.array.color_array)
                else context.resources.obtainTypedArray(R.array.pattern_array)

            val colorImgResource = arrayOfColors.getResourceId(position,0)
            holder.colorCell.setImageResource(colorImgResource)
            holder.colorCell.setOnClickListener{listener.onColorClick(colorImgResource)}
        }
    }

    override fun getItemCount(): Int
    {
        return if(context != null)
        {
            val arrayOfSelectedMode =
                if (mode == "Color") context.resources.obtainTypedArray(R.array.color_array)
                else context.resources.obtainTypedArray(R.array.pattern_array)
            val numOfItemsInSelectedMode = arrayOfSelectedMode.length()
            arrayOfSelectedMode.recycle()

            numOfItemsInSelectedMode
        } else 0
    }

}