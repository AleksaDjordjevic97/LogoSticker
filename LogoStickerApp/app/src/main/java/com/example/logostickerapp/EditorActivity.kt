package com.example.logostickerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.logostickerapp.databinding.ActivityEditorBinding

class EditorActivity : AppCompatActivity()
{

    private lateinit var binding: ActivityEditorBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



    }

    fun backEditorButtonClick(backButton: View)
    {
        finish()
    }

    fun bgdButtonClick(bgdButton: View)
    {

    }

    fun logoButtonClick(logoButton: View)
    {

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
}