package com.example.logostickerapp.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.example.logostickerapp.databinding.FragmentPhotoBinding
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


private const val SELECT_IMAGE_GALLERY = 1
private const val SELECT_IMAGE_CAMERA = 2

class PhotoFragment(private val listener: PhotoFragmentListener) : DialogFragment()
{
    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentPhotoPath: String

    interface PhotoFragmentListener
    {
        fun onGetPhoto(photoUri: Uri, mode:String?)
    }

    override fun onStart()
    {
        super.onStart()
        val dialog: Dialog? = getDialog()

        if (dialog != null)
        {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width,height)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setDimAmount(0.85f)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        _binding = FragmentPhotoBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.root.setOnClickListener{dismiss()}
        binding.btnCamera.setOnClickListener{btnCameraClick()}
        binding.btnGallery.setOnClickListener{btnGalleryClick()}

    }

    @Throws(IOException::class)
    private fun createImageFile(): File
    {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply{
            currentPhotoPath = absolutePath
        }
    }

    private fun btnCameraClick()
    {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            context?.let {
                takePictureIntent.resolveActivity(it.packageManager)?.also {

                    val photoFile: File? = try
                    {
                        createImageFile()
                    } catch (e: IOException)
                    {
                        null
                    }

                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            requireContext(),
                            "com.example.logostickerapp.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, SELECT_IMAGE_CAMERA)
                    }
                }
            }
        }
    }

    private fun btnGalleryClick()
    {
       val galleryIntent = Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, SELECT_IMAGE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK)
        {
            when(requestCode)
            {
                SELECT_IMAGE_GALLERY ->
                {
                    try
                    {
                        val uri = data?.data
                        if (uri != null)
                        {

                            listener.onGetPhoto(uri,tag)
                        }
                        dismiss()

                    }
                    catch (e:Exception)
                    {
                        e.printStackTrace()
                    }


                }

                SELECT_IMAGE_CAMERA ->
                {
                    val f = File(currentPhotoPath)
                    val uri = Uri.fromFile(f)
                    if (uri != null)
                    {
                        listener.onGetPhoto(uri,tag)
                    }
                    dismiss()
                }
            }
        }
    }
}