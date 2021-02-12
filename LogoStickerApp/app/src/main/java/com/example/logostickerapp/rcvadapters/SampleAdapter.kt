package com.example.logostickerapp.rcvadapters

import android.content.Context
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.logostickerapp.R

class SampleAdapter(private val context: Context,
                    private val sampleIcons: Int,
                    private val listener: OnSampleClickListener,
                    private val mode:String,
                    private val categoryPos:Int): RecyclerView.Adapter<SampleAdapter.SampleViewHolder>()
{

    interface OnSampleClickListener
    {
        fun onSampleClick(samplePos: Int, categoryPos: Int, mode: String)
    }

    inner class SampleViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val imgSample: ImageView = view.findViewById(R.id.imgSample)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_sample,parent,false)
        return SampleViewHolder(view)
    }

    override fun onBindViewHolder(holder: SampleViewHolder, position: Int)
    {
        val sampleArray: TypedArray = context.resources.obtainTypedArray(sampleIcons)
        holder.imgSample.setImageResource(sampleArray.getResourceId(position,0))
        holder.imgSample.setOnClickListener { listener.onSampleClick(position,categoryPos,mode) }
        sampleArray.recycle()
    }

    override fun getItemCount(): Int
    {
        val sampleIconsArray = context.resources.obtainTypedArray(sampleIcons)
        val itemCount = sampleIconsArray.length()
        sampleIconsArray.recycle()

        return itemCount
    }


}