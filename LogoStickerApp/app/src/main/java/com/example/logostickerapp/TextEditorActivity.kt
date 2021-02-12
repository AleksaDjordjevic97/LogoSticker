package com.example.logostickerapp

import android.content.Context
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.replace
import com.example.logostickerapp.databinding.ActivityTextEditorBinding
import com.example.logostickerapp.fragments.ColorFragment
import com.example.logostickerapp.fragments.FontFragment
import com.example.logostickerapp.fragments.KeyboardFragment
import com.example.logostickerapp.rcvadapters.FontAdapter

class TextEditorActivity : AppCompatActivity(), KeyboardFragment.FragmentKeyboardListener, FontAdapter.OnFontClickListener
{

    private lateinit var binding:ActivityTextEditorBinding
    private val keyboardFragment = KeyboardFragment(this)
    private lateinit var fontFragment:FontFragment
    private lateinit var colorFragment:ColorFragment
    private lateinit var inputMethodManager: InputMethodManager
    private var currentText = ""
    private var currentFont = "Original"



    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityTextEditorBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.txtInputTE.isEnabled = true
        binding.txtInputTE.isFocusableInTouchMode = true
        binding.txtInputTE.isFocusable = true
        binding.txtInputTE.setEnableSizeCache(false)
        binding.txtInputTE.movementMethod = null

        binding.txtInputTE.setOnFocusChangeListener{ view: View, hasFocus: Boolean -> if(hasFocus) openKeyboard()}

    }

    fun btnBackTEClick(btnBackTE: View)
    {
        finish()
    }

    fun btnDoneTEClick(btnDoneTE:View)
    {

    }

    fun btnFontTEClick(btnFontTE:View)
    {
        fontFragment= FontFragment(this,currentFont)
        supportFragmentManager.beginTransaction().replace(R.id.frameFragmentTE,fontFragment).commit()
    }

    override fun onFontClick(font: Typeface, fontName: String)
    {
        binding.txtInputTE.typeface = font
        currentFont = fontName
    }

    fun btnColorTEClick(btnColorTE:View)
    {

    }

    private fun openKeyboard()
    {
        binding.txtInputTE.isFocusable = true
        currentText = binding.txtInputTE.text.toString()
        binding.txtInputTE.requestFocus()
        inputMethodManager.showSoftInput(binding.txtInputTE,InputMethodManager.SHOW_IMPLICIT)
        supportFragmentManager.beginTransaction().replace(R.id.frameFragmentTE,keyboardFragment).commit()
    }

    override fun onKeyboardChange(change: Boolean)
    {
        inputMethodManager.hideSoftInputFromWindow(binding.txtInputTE.windowToken,0)
        binding.txtInputTE.clearFocus()

        if(!change)
            binding.txtInputTE.setText(currentText)
    }




}