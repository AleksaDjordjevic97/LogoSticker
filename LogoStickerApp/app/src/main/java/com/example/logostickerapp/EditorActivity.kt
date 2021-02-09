package com.example.logostickerapp

import android.content.res.TypedArray
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.logostickerapp.databinding.ActivityEditorBinding
import com.example.logostickerapp.fragments.PhotoFragment
import com.example.logostickerapp.rcvadapters.CategoryAdapter
import com.example.logostickerapp.rcvadapters.SampleAdapter

class EditorActivity : AppCompatActivity(), CategoryAdapter.OnCategoryClickListener, SampleAdapter.OnSampleClickListener, PhotoFragment.PhotoFragmentListener
{

    private lateinit var binding: ActivityEditorBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var sampleAdapter: SampleAdapter
    private val linearLayoutManagerCategory = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
    private val linearLayoutManagerSample = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)


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

       //DA SE ZACRVENI KADA SE SELEKTUJE

    }

    fun logoButtonClick(logoButton: View)
    {
        categoryAdapter = CategoryAdapter(this,R.array.logo_categories,this, "Logo")
        binding.rcvCategoryEditor.adapter = categoryAdapter
        binding.rcvSamplesEditor.visibility = View.GONE

        //DA SE ZACRVENI KADA SE SELEKTUJE
    }

    fun textButtonClick(textButton: View)
    {
        TODO("OTVORI TEKST EDITOR")
    }

    fun imageButtonClick(imageButton: View)
    {
        val photoFragment = PhotoFragment(this)
        photoFragment.show(supportFragmentManager,"Sticker")

        //DA SE ZACRVENI KADA SE SELEKTUJE
    }


    fun doneEditorButtonClick(doneButton: View)
    {
        TODO("ZAVRSENO EDITOVANJE SLIKE I VRACA SE NA POCETNI EKRAN")
    }


    override fun onCategoryClick(position: Int, mode: String)
    {

        if (mode == "Bgd")
        {
            val numOfCategories = resources.obtainTypedArray(R.array.bgd_categories).length()

            if (position != numOfCategories - 1)
                setSamples(position, mode)
            else
            {
                val photoFragment = PhotoFragment(this)
                photoFragment.show(supportFragmentManager, "Bgd")
            }
        }
        else
            setSamples(position, mode)

    }

    private fun setSamples(position: Int, mode: String)
    {
        val sampleIconResArray:TypedArray = if(mode == "Bgd")
            resources.obtainTypedArray(R.array.bgd_icon_array)
        else
            resources.obtainTypedArray(R.array.logo_icon_array)

        val selectedResArray = sampleIconResArray.getResourceId(position, 0)
        sampleAdapter = SampleAdapter(this, selectedResArray, this, mode, position)

        binding.rcvSamplesEditor.adapter = sampleAdapter
        binding.rcvSamplesEditor.visibility = View.VISIBLE
    }

    override fun onSampleClick(samplePos: Int, categoryPos: Int, mode: String)
    {

        val sampleByCategoryArray = if(mode.equals("Bgd"))
            resources.obtainTypedArray(R.array.bgd_array)
        else
            resources.obtainTypedArray(R.array.logo_array)

        val selectedSampleByCategory = sampleByCategoryArray.getResourceId(categoryPos,0)
        val samplePicturesArray = resources.obtainTypedArray(selectedSampleByCategory)
        val selectedPictureRes = samplePicturesArray.getResourceId(samplePos,0)

        if(mode == "Bgd")
            Glide.with(this).load(selectedPictureRes).into(binding.imgLogoEditor)
        else if(mode == "Logo")
        {
            //AKO JE LOGO DA NAPRAVI STIKER
        }


    }

    override fun onGetPhoto(photoUri: Uri, mode: String?)
    {
        if(mode == "Bgd")
            Glide.with(this).load(photoUri).into(binding.imgLogoEditor)
        else if(mode == "Sticker")
        {
            //AKO JE IMAGE DA NAPRAVI STIKER
        }
    }


}