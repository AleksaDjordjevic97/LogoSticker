package com.example.logostickerapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.logostickerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity()
{

    private lateinit var binding: ActivityMainBinding
    private val MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 100
    private val CREATE_A_LOGO_REQUEST_CODE = 101


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        checkPermissions()
    }

    fun privacyPolicyButtonClick(privacyPolicyButton: View)
    {
        val url = "https://en.wikipedia.org/wiki/Privacy_policy"
        val privacyPolicyIntent = Intent(Intent.ACTION_VIEW)
        privacyPolicyIntent.setData(Uri.parse(url))
        startActivity(privacyPolicyIntent)
    }

    fun shareButtonClick(shareButton: View)
    {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Product Page")
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.shopify.com/blog/product-page#:~:text=A%20product%20page%20is%20a,and%20facilitate%20the%20buying%20process.")
        startActivity(Intent.createChooser(shareIntent, "Share Product Page"))
    }

    fun createALogoButtonClick(createALogoButton: View)
    {
        val editorIntent = Intent(this,EditorActivity::class.java)
        startActivityForResult(editorIntent,CREATE_A_LOGO_REQUEST_CODE)
    }

    fun checkPermissions():Boolean
    {
        if(ContextCompat.checkSelfPermission(applicationContext,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE)
            return false
        }
        else
            return true

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK)
        {
            when(requestCode)
            {
                CREATE_A_LOGO_REQUEST_CODE ->
                {

                    TODO("PREUZMI SLIKU I STAVI NA IMAGEVIEW ZA LOGO")

                }
            }
        }


    }
}