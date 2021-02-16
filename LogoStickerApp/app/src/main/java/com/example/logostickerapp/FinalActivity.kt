package com.example.logostickerapp

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.logostickerapp.databinding.ActivityFinalBinding
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

class FinalActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityFinalBinding
    private var logoBitmap: Bitmap? = null
    private var imgUriForShare: Uri? = null
    private val REQUEST_DELETE_DRAFT = 1



    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityFinalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadLogoImage()
    }

    fun btnBackFinalClick(btnBackFinal: View)
    {
        finish()
    }

    fun btnHomeFinalClick(btnHomeFinal: View)
    {
        val homeIntent = Intent(applicationContext, MainActivity::class.java)
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        homeIntent.putExtra("EXIT", true)
        startActivity(homeIntent)
        finish()
    }

    fun btnSaveFinalClick(btnSaveFinal: View)
    {
        if (logoBitmap != null)
        {
            Runnable {
                saveToGallery(logoBitmap!!)
                runOnUiThread{Toast.makeText(
                    applicationContext,
                    "Logo saved to Gallery!",
                    Toast.LENGTH_SHORT
                ).show()}
            }.run()
        }
    }

    fun btnShareFinalClick(btnShareFinal: View)
    {
        if(logoBitmap != null)
        {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/png"
            imgUriForShare = createTempImageFile(logoBitmap!!)
            shareIntent.putExtra(Intent.EXTRA_STREAM, imgUriForShare)
            startActivityForResult(
                Intent.createChooser(shareIntent, "Share Image"),
                REQUEST_DELETE_DRAFT
            )
        }

    }

    private fun loadLogoImage()
    {
        val logoFilename = intent.getStringExtra("FINAL_IMAGE")
        try
        {
            val inputStream = this.openFileInput(logoFilename)
            logoBitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            Glide.with(this).load(logoBitmap).into(binding.imgLogoFinal)
        } catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun saveToGallery(bitmap: Bitmap)
    {
        val filename = "${System.currentTimeMillis()}.png"
        val albumName = "LogoStickerApp"
        val write: (OutputStream) -> Boolean = {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    "${Environment.DIRECTORY_PICTURES}/$albumName"
                )
            }

            applicationContext.contentResolver.let {
                it.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    ?.let { uri ->
                        it.openOutputStream(uri)?.let(write)
                    }
            }
        } else
        {
            val imagesDir =
                applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    .toString() + separator + albumName
            val file = File(imagesDir)
            if (!file.exists())
            {
                file.mkdir()
            }
            val image = File(imagesDir, filename)
            write(FileOutputStream(image))
        }
    }

    private fun createTempImageFile(bitmap: Bitmap): Uri?
    {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "title")
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        val uri = contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )

        try
        {
            val outstream = contentResolver.openOutputStream(uri!!)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outstream)
            outstream!!.close()
        } catch (e: Exception)
        {
            e.printStackTrace()
        }
        return uri
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_DELETE_DRAFT)
        {
            try
            {
                val contentResolver = contentResolver
                contentResolver.delete(imgUriForShare!!, null, null)
            } catch (e: java.lang.Exception)
            {
                e.printStackTrace()
            }
        }
    }
}

