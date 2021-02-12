package com.example.logostickerapp.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.logostickerapp.databinding.FragmentKeyboardBinding


class KeyboardFragment(private val listener: FragmentKeyboardListener) : DialogFragment()
{
    private var _binding: FragmentKeyboardBinding? = null
    private val binding get() = _binding!!

    interface FragmentKeyboardListener
    {
        fun onKeyboardChange(change: Boolean)
    }

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
    ): View?
    {
        _binding = FragmentKeyboardBinding.inflate(inflater,container,false)
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

        binding.btnKeyboardCancel.setOnClickListener{keyboardChangeText(false)}
        binding.btnKeyboardDone.setOnClickListener({keyboardChangeText(true)})

    }

    private fun keyboardChangeText(change: Boolean)
    {
        listener.onKeyboardChange(change)
        dismiss()
    }

}