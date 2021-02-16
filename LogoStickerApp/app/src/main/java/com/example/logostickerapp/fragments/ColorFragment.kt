package com.example.logostickerapp.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.logostickerapp.R
import com.example.logostickerapp.databinding.FragmentColorBinding
import com.example.logostickerapp.rcvadapters.ColorAdapter

class ColorFragment(private val  listener: ColorAdapter.OnColorClickListener, private val oldColor:Int) : DialogFragment()
{
    private var _binding:FragmentColorBinding? = null
    private val binding get() = _binding!!
    private lateinit var gridLayoutManagerColor:GridLayoutManager

    override fun onStart()
    {
        super.onStart()
        val dialog: Dialog? = dialog
        setupDialog(dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        _binding = FragmentColorBinding.inflate(inflater,container,false)
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

        initializeLayoutManager()
        setLayoutManager()
        setAdapter("Pattern")

        binding.btnColorDone.setOnClickListener { dismiss() }
        binding.btnColorCancel.setOnClickListener { revertColorChange() }
        binding.btnColorPattern.setOnClickListener { setAdapter("Pattern") }
        binding.btnColorColor.setOnClickListener { setAdapter("Color") }

    }

    private fun setupDialog(dialog:Dialog?)
    {
        if(dialog != null)
        {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private fun initializeLayoutManager()
    {
        val spanCount = calculateSpanCountForGrid()
        gridLayoutManagerColor = GridLayoutManager(context,spanCount)
    }

    private fun calculateSpanCountForGrid():Int
    {
        if(context != null)
        {
            val displayMetrics = resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            return (screenWidth - 140) / 230
        }else return 0
    }

    private fun setLayoutManager()
    {
        binding.rcvColorPattern.layoutManager = gridLayoutManagerColor
    }

    private fun setAdapter(mode:String)
    {
        if (mode == "Color")
            binding.rcvColorPattern.adapter = ColorAdapter(context, "Color", listener)
        else if(mode == "Pattern")
            binding.rcvColorPattern.adapter = ColorAdapter(context, "Pattern", listener)

        changeActiveButton(mode)
    }

    private fun changeActiveButton(mode: String)
    {
        if (mode == "Color")
        {
            binding.btnColorColor.setImageResource(R.drawable.solid_btn_active)
            binding.btnColorPattern.setImageResource(R.drawable.pattern_btn)
        }
        else if(mode == "Pattern")
        {
            binding.btnColorColor.setImageResource(R.drawable.solid_btn)
            binding.btnColorPattern.setImageResource(R.drawable.pattern_btn_active)
        }
    }


    private fun revertColorChange()
    {
        listener.onColorClick(oldColor)
        dismiss()
    }
}