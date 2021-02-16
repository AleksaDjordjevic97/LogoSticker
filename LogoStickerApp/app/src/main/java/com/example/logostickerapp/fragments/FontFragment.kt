package com.example.logostickerapp.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.logostickerapp.databinding.FragmentFontBinding
import com.example.logostickerapp.rcvadapters.FontAdapter

class FontFragment(private val  listener:FontAdapter.OnFontClickListener, private val oldFont:String) : DialogFragment()
{
    private var _binding: FragmentFontBinding? = null
    private val binding get() = _binding!!
    private val linearLayoutManagerFont = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)

    override fun onStart()
    {
        super.onStart()
        val dialog: Dialog? = dialog

        if (dialog != null)
        {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width,height)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        _binding = FragmentFontBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.rcvFont.layoutManager = linearLayoutManagerFont
        binding.rcvFont.adapter = FontAdapter(context,listener)

        binding.btnFontDone.setOnClickListener { dismiss() }
        binding.btnFontCancel.setOnClickListener { revertFont() }

    }

    private fun revertFont()
    {
        val oldFontTF = if(oldFont == "Original") Typeface.DEFAULT else Typeface.createFromAsset(context?.assets,oldFont)
        listener.onFontClick(oldFontTF,oldFont)
        dismiss()
    }
}