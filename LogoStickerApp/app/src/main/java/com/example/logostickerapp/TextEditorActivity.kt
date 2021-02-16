package com.example.logostickerapp

import android.content.Context
import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.logostickerapp.customclasses.TextStickerInfo
import com.example.logostickerapp.databinding.ActivityTextEditorBinding
import com.example.logostickerapp.fragments.ColorFragment
import com.example.logostickerapp.fragments.FontFragment
import com.example.logostickerapp.fragments.KeyboardFragment
import com.example.logostickerapp.rcvadapters.ColorAdapter
import com.example.logostickerapp.rcvadapters.FontAdapter

class TextEditorActivity : AppCompatActivity(), KeyboardFragment.FragmentKeyboardListener, FontAdapter.OnFontClickListener, ColorAdapter.OnColorClickListener
{

    private lateinit var binding:ActivityTextEditorBinding
    private val keyboardFragment = KeyboardFragment(this)
    private lateinit var fontFragment:FontFragment
    private lateinit var colorFragment:ColorFragment
    private lateinit var inputMethodManager: InputMethodManager
    private var currentText = ""
    private var currentFont = "Original"
    private var currentColor = R.drawable.solid_1



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

        setInitialText()
    }

    fun btnBackTEClick(btnBackTE: View)
    {
        finish()
    }

    fun btnDoneTEClick(btnDoneTE:View)
    {
        val rawTextContent = binding.txtInputTE.text.toString()
        val linesInRawTextContent = binding.txtInputTE.lineCount
        val editedTextContent = insertNewLineCharsInText(rawTextContent,linesInRawTextContent)
        val textSize = binding.txtInputTE.textSize
        val textInfo = TextStickerInfo(editedTextContent,currentFont,textSize,currentColor)
        val doneEditingTextIntent = Intent()
        doneEditingTextIntent.putExtra("TEXT_STICKER_INFO",textInfo)
        setResult(RESULT_OK,doneEditingTextIntent)
        finish()

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
        colorFragment = ColorFragment(this,currentColor)
        supportFragmentManager.beginTransaction().replace(R.id.frameFragmentTE,colorFragment).commit()
    }

    override fun onColorClick(colorImgResource: Int)
    {
        currentColor = colorImgResource
        val colorBitmap = BitmapFactory.decodeResource(resources,colorImgResource)
        val shader = BitmapShader(colorBitmap,Shader.TileMode.REPEAT,Shader.TileMode.REPEAT)
        binding.txtInputTE.paint.shader = shader
        val originalText = binding.txtInputTE.text.toString()
        binding.txtInputTE.setText(originalText)
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

    private fun setInitialText()
    {
        val textInfo = intent.getSerializableExtra("TEXT_STICKER_INFO") as TextStickerInfo
        binding.txtInputTE.setText(textInfo.content)
        //MOZDA SIZE
        onFontClick(getTypefaceFromName(textInfo.fontName),textInfo.fontName)
        onColorClick(textInfo.colorResource)
    }

    private fun getTypefaceFromName(fontName:String):Typeface
    {
        return if(fontName == "Original")
            Typeface.DEFAULT
        else
            Typeface.createFromAsset(assets,fontName)
    }

    private fun insertNewLineCharsInText(inputText:String,lines:Int):String
    {
        var resultText = inputText

        if(lines > 1)
        {
            var nCounter = 0

            for(i in 0 until lines-1)
            {
                val before:String
                val after:String

                val end = binding.txtInputTE.layout.getLineEnd(i)+nCounter
                if(resultText.substring(end-1,end) != "\n")
                {
                    before = resultText.substring(0,end)
                    after = resultText.substring(end)
                    resultText = before + "\n" + after
                    nCounter++
                }
            }
        }
        return resultText
    }




}