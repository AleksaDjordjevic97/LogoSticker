package com.example.logostickerapp.rcvadapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.logostickerapp.R

class FontAdapter(private val context: Context?,
                  private val listener:OnFontClickListener): RecyclerView.Adapter<FontAdapter.FontViewHolder>()
{


    interface OnFontClickListener
    {
        fun onFontClick(font:Typeface, fontName:String)
    }

    inner class FontViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val fontCell: TextView = itemView.findViewById(R.id.font_cell)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_font,parent,false)
        return FontViewHolder(view)
    }

    override fun onBindViewHolder(holder: FontViewHolder, position: Int)
    {
        if(context != null)
        {
            val fontNameArray = context.resources.getStringArray(R.array.fonts_array)
            var fontName:String
            val font:Typeface

            if (position == 0)
            {
                fontName = "Original"
                font = Typeface.DEFAULT
            } else
            {
                fontName = fontNameArray[position - 1]
                font = Typeface.createFromAsset(context.assets, fontName)
            }
            holder.fontCell.text = context.getString(R.string.font_cell) + " " + (position + 1)
            holder.fontCell.typeface = font

            holder.fontCell.setOnClickListener { listener.onFontClick(font, fontName) }
        }
    }

    override fun getItemCount(): Int
    {
        return if (context != null) context.resources.getStringArray(R.array.fonts_array).size else 0
    }


}