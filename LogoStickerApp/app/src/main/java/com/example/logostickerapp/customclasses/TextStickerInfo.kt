package com.example.logostickerapp.customclasses

import java.io.Serializable

class TextStickerInfo constructor(var content:String,
                        var fontName:String,
                        var textSize:Float,
                        var colorResource:Int) : Serializable
{
    init
    {

    }
}