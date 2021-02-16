package com.example.logostickerapp

import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.graphics.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.core.graphics.createBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.logostickerapp.customclasses.TextStickerInfo
import com.example.logostickerapp.customview.StickerView
import com.example.logostickerapp.databinding.ActivityEditorBinding
import com.example.logostickerapp.fragments.PhotoFragment
import com.example.logostickerapp.rcvadapters.CategoryAdapter
import com.example.logostickerapp.rcvadapters.SampleAdapter
import java.lang.Exception

class EditorActivity : AppCompatActivity(),
                        CategoryAdapter.OnCategoryClickListener,
                        SampleAdapter.OnSampleClickListener,
                        PhotoFragment.PhotoFragmentListener,
                        StickerView.StickerViewListener
{

    private val TEXT_NEW_REQUEST_CODE = 100
    private val TEXT_EDIT_REQUEST_CODE = 101
    private lateinit var binding: ActivityEditorBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var sampleAdapter: SampleAdapter
    private val linearLayoutManagerCategory = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
    private val linearLayoutManagerSample = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
    private lateinit var viewGroup:ViewGroup
    private val stickersOnScreen:ArrayList<StickerView> = ArrayList()
    private var selectedSticker:StickerView? = null
    private var textInfoMap:HashMap<StickerView,TextStickerInfo> = HashMap()



    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.rcvCategoryEditor.layoutManager = linearLayoutManagerCategory
        binding.rcvSamplesEditor.layoutManager = linearLayoutManagerSample

        binding.btnBgdEditor.performClick()
        viewGroup = binding.frmImageLayout

    }

    fun backEditorButtonClick(backButton: View)
    {
        finish()
    }

    fun bgdButtonClick(bgdButton: View)
    {
        categoryAdapter = CategoryAdapter(this,this, "Bgd")

        binding.rcvCategoryEditor.adapter = categoryAdapter
        binding.rcvSamplesEditor.visibility = View.GONE
        deselectTypeButtons()
        binding.btnBgdEditor.setImageResource(R.drawable.bgd_btn_active)
    }

    fun logoButtonClick(logoButton: View)
    {
        categoryAdapter = CategoryAdapter(this,this, "Logo")

        binding.rcvCategoryEditor.adapter = categoryAdapter
        binding.rcvSamplesEditor.visibility = View.GONE
        deselectTypeButtons()
        binding.btnLogoEditor.setImageResource(R.drawable.logo_btn_active)
    }

    fun textButtonClick(textButton: View)
    {
        val textIntent = Intent(this,TextEditorActivity::class.java)

        if(selectedSticker != null && selectedSticker!!.isTextSticker)
        {
            val textInfo = textInfoMap[selectedSticker!!]
            textIntent.putExtra("TEXT_STICKER_INFO",textInfo)
            startActivityForResult(textIntent,TEXT_EDIT_REQUEST_CODE)
        }
        else
        {
            val textInfo = TextStickerInfo("","Original",90f,R.drawable.solid_1)
            textIntent.putExtra("TEXT_STICKER_INFO",textInfo)
            startActivityForResult(textIntent,TEXT_NEW_REQUEST_CODE)
        }

        deselectTypeButtons()
        binding.btnTextEditor.setImageResource(R.drawable.text_btn_active)
    }

    fun imageButtonClick(imageButton: View)
    {
        val photoFragment = PhotoFragment(this)
        photoFragment.show(supportFragmentManager,"Sticker")

        deselectTypeButtons()
        binding.btnImageEditor.setImageResource(R.drawable.image_btn_active)
    }


    fun doneEditorButtonClick(doneButton: View)
    {
        onDeselectSticker()

        val finalLogoBitmap = createBitmap(viewGroup.width,viewGroup.height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(finalLogoBitmap)
        viewGroup.draw(canvas)

        try
        {
            val filename = "bitmap.png"
            val stream = this.openFileOutput(filename, Context.MODE_PRIVATE)
            finalLogoBitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
            stream.close()
            finalLogoBitmap.recycle()

            val finalIntent = Intent(this,FinalActivity::class.java)
            finalIntent.putExtra("FINAL_IMAGE",filename)
            startActivity(finalIntent)
        }catch (e:Exception)
        {
            e.printStackTrace()
        }
    }

    fun frameViewClick(frameView: View)
    {
        onDeselectSticker()
    }

    private fun deselectTypeButtons()
    {
        binding.btnBgdEditor.setImageResource(R.drawable.bgd_btn)
        binding.btnLogoEditor.setImageResource(R.drawable.logo_btn)
        binding.btnTextEditor.setImageResource(R.drawable.text_btn)
        binding.btnImageEditor.setImageResource(R.drawable.image_btn)
    }


    override fun onCategoryClick(position: Int, mode: String)
    {

        if (mode == "Bgd")
        {
            val bgdCategoryArray = resources.obtainTypedArray(R.array.bgd_categories)
            val numOfCategories = bgdCategoryArray.length()

            if (position != numOfCategories - 1)
                setSamples(position, mode)
            else
            {
                val photoFragment = PhotoFragment(this)
                photoFragment.show(supportFragmentManager, "Bgd")
            }
            bgdCategoryArray.recycle()
        }
        else
            setSamples(position, mode)

        categoryAdapter.setActiveCategory(position,mode)


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

        sampleIconResArray.recycle()
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
            createSticker(selectedPictureRes,false)
        }

        sampleByCategoryArray.recycle()
        samplePicturesArray.recycle()
    }

    override fun onGetPhoto(photoUri: Uri, mode: String?)
    {
        if(mode == "Bgd")
            Glide.with(this).load(photoUri).into(binding.imgLogoEditor)
        else if(mode == "Sticker")
            createSticker(photoUri,false)

    }

    private fun createSticker(pictureRes:Int, isTextSticker:Boolean)
    {
        val newStickerView = StickerView(this,this,isTextSticker)
        newStickerView.setPicture(pictureRes)
        addSticker(newStickerView)
    }

    private fun createSticker(pictureUri:Uri, isTextSticker:Boolean)
    {
        val newStickerView = StickerView(this,this,isTextSticker)
        newStickerView.setPicture(pictureUri)
        addSticker(newStickerView)
    }

    private fun addSticker(newStickerView:StickerView)
    {
        viewGroup.addView(newStickerView)
        stickersOnScreen.add(newStickerView)
        newStickerView.selectSticker()
        selectedSticker = newStickerView

        newStickerView.waitForLayout { newStickerView.centerView(viewGroup.width / 2, viewGroup.height / 2)}
    }

    private inline fun View.waitForLayout(crossinline f: () -> Unit) = with(viewTreeObserver) {
        addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                removeOnGlobalLayoutListener(this)
                f()
            }
        })
    }

    override fun onSelectSticker(sticker: StickerView)
    {
        if(selectedSticker!=sticker)
        {
            sticker.selectSticker()
            selectedSticker = sticker
        }
    }

    override fun onDeselectSticker()
    {
        selectedSticker?.deselectSticker()
        selectedSticker = null
    }

    override fun onRemoveSticker(sticker: StickerView)
    {
        selectedSticker = null
        viewGroup.removeView(sticker)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK)
        {
            when(requestCode)
            {
                TEXT_NEW_REQUEST_CODE ->
                {
                    val textInfo:TextStickerInfo = data?.getSerializableExtra("TEXT_STICKER_INFO") as TextStickerInfo
                    createTextSticker(textInfo)
                }
                TEXT_EDIT_REQUEST_CODE ->
                {
                    val textInfo:TextStickerInfo = data?.getSerializableExtra("TEXT_STICKER_INFO") as TextStickerInfo
                    updateTextSticker(textInfo)
                }
            }
        }
    }

    private fun createTextSticker(textInfo:TextStickerInfo)
    {
        val stickerView = StickerView(this,this, true)
        convertTextToBitmap(stickerView,textInfo)
        addSticker(stickerView)
    }

    private fun convertTextToBitmap(stickerView:StickerView,textInfo:TextStickerInfo)
    {
        val textViewTemp = TextView(this)
        textViewTemp.setLayerType(View.LAYER_TYPE_SOFTWARE,null)

        if(textInfoMap.containsKey(stickerView))
            textInfoMap.remove(stickerView)

        textInfoMap[stickerView] = textInfo

        textViewTemp.text = textInfo.content
        textViewTemp.setTextSize(TypedValue.COMPLEX_UNIT_PX,300f)
        textViewTemp.typeface = getTypefaceFromName(textInfo.fontName)
        textViewTemp.setTextColor(Color.BLACK)
        setColorPattern(textViewTemp,textInfo.colorResource)
        textViewTemp.textAlignment = View.TEXT_ALIGNMENT_CENTER
        viewGroup.addView(textViewTemp)

        textViewTemp.measure(View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED))
        textViewTemp.layout(0,0,textViewTemp.measuredWidth,textViewTemp.measuredHeight)

        val textBitmap = Bitmap.createBitmap(textViewTemp.measuredWidth,textViewTemp.measuredHeight,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(textBitmap)
        textViewTemp.draw(canvas)

        stickerView.setPicture(textBitmap)

        viewGroup.removeView(textViewTemp)

    }

    private fun getTypefaceFromName(fontName:String):Typeface
    {
        return if(fontName == "Original")
            Typeface.DEFAULT
        else
            Typeface.createFromAsset(assets,fontName)
    }

    private fun setColorPattern(textView:TextView,imgRes:Int)
    {
        val colorBitmap = BitmapFactory.decodeResource(resources,imgRes)
        val shader = BitmapShader(colorBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        textView.paint.shader = shader
        val originalText = textView.text.toString()
        textView.text = originalText
    }

    private fun updateTextSticker(textInfo: TextStickerInfo)
    {
        selectedSticker?.let { convertTextToBitmap(it, textInfo) }
    }

}

