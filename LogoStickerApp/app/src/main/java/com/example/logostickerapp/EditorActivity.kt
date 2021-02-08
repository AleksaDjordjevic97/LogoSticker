package com.example.logostickerapp

import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.logostickerapp.databinding.ActivityEditorBinding
import com.example.logostickerapp.rcvadapters.CategoryAdapter
import com.example.logostickerapp.rcvadapters.SampleAdapter

class EditorActivity : AppCompatActivity(), CategoryAdapter.OnCategoryClickListener, SampleAdapter.OnSampleClickListener
{

    private lateinit var binding: ActivityEditorBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var sampleAdapter: SampleAdapter
    val linearLayoutManagerCategory = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
    val linearLayoutManagerSample = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.rcvCategoryEditor.layoutManager = linearLayoutManagerCategory
        binding.rcvSamplesEditor.layoutManager = linearLayoutManagerSample

        binding.btnBgdEditor.performClick()



    }

    fun backEditorButtonClick(backButton: View)
    {
        finish()
    }

    fun bgdButtonClick(bgdButton: View)
    {
        categoryAdapter = CategoryAdapter(this,R.array.bgd_categories,this, "Bgd")
        binding.rcvCategoryEditor.adapter = categoryAdapter
        binding.rcvSamplesEditor.visibility = View.GONE

    }

    fun logoButtonClick(logoButton: View)
    {
        categoryAdapter = CategoryAdapter(this,R.array.logo_categories,this, "Logo")
        binding.rcvCategoryEditor.adapter = categoryAdapter
        binding.rcvSamplesEditor.visibility = View.GONE
    }

    fun textButtonClick(textButton: View)
    {

    }

    fun imageButtonClick(imageButton: View)
    {

    }


    fun doneEditorButtonClick(doneButton: View)
    {

    }


    override fun onCategoryClick(position: Int, mode: String)                           //PROVERI DA LI NIJE PHOTO OPCIJA
    {
        val sampleIconResArray:TypedArray

        if(mode.equals("Bgd"))
            sampleIconResArray = resources.obtainTypedArray(R.array.bgd_icon_array)
        else
            sampleIconResArray = resources.obtainTypedArray(R.array.logo_icon_array)

        val selectedResArray = sampleIconResArray.getResourceId(position,0)
        sampleAdapter = SampleAdapter(this,selectedResArray,this,mode,position)

        binding.rcvSamplesEditor.adapter = sampleAdapter
        binding.rcvSamplesEditor.visibility = View.VISIBLE


    }

    override fun onSampleClick(samplePos: Int, categoryPos: Int, mode: String)
    {
        val sampleByCategoryArray:TypedArray
        val selectedSampleByCategory:Int
        val samplePicturesArray:TypedArray
        val selectedPictureRes:Int

        if(mode.equals("Bgd"))
            sampleByCategoryArray = resources.obtainTypedArray(R.array.bgd_array)
        else
            sampleByCategoryArray = resources.obtainTypedArray(R.array.logo_array)          //AKO JE LOGO DODAJ NA EKRAN, NE DA ZAMENI POZADINU

        selectedSampleByCategory = sampleByCategoryArray.getResourceId(categoryPos,0)
        samplePicturesArray = resources.obtainTypedArray(selectedSampleByCategory)
        selectedPictureRes = samplePicturesArray.getResourceId(samplePos,0)

        Glide.with(this).load(selectedPictureRes).into(binding.imgLogoEditor)


    }


}