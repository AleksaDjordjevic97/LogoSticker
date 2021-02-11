package com.example.logostickerapp.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.bumptech.glide.Glide
import com.example.logostickerapp.R
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class StickerView @JvmOverloads constructor(
    context: Context,
    private val listener: StickerViewListener,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0

) : FrameLayout(context, attrs, defStyle, defStyleRes)
{
    private val DEFAULT_WIDTH = 300
    private val DEFAULT_HEIGHT = 300
    private val MAX_SIZE = 900
    private var mX: Float = 0f
    private var mY: Float = 0f
    private var sX: Float = 0f
    private var sY: Float = 0f
    private var nX: Float = 0f
    private var nY: Float = 0f
    private var currentHeight: Int = 0
    private var currentWidth: Int = 0
    private var modeMove = true
    private var mPtrID1: Int = -1
    private var mPtrID2: Int = -1
    private var mAngle: Float = 0f
    private var oldRotation: Float? = null
    private val mFPoint: PointF = PointF()
    private val mSPoint: PointF = PointF()
    private val INVALID_POINTER_ID = -1
    private val stickerImage: ImageFilterView
    private val btnRemove: ImageButton
    private val btnMirror: ImageButton
    private val btnScale: ImageButton


    interface StickerViewListener
    {
        fun onSelectSticker(sticker: StickerView)
        fun onRemoveSticker(sticker: StickerView)
        fun onDeselectSticker()
    }

    init
    {
        inflate(context, R.layout.frame_sticker, this)
        layoutParams = LayoutParams(DEFAULT_WIDTH, DEFAULT_HEIGHT)
        stickerImage = findViewById(R.id.imgFrameSticker)
        btnRemove = findViewById(R.id.btnRemoveSticker)
        btnMirror = findViewById(R.id.btnMirrorSticker)
        btnScale = findViewById(R.id.btnScaleSticker)

        btnRemove.setOnClickListener { removeSticker() }
        btnMirror.setOnClickListener { mirrorSticker() }
        btnScale.setOnTouchListener({ view: View, motionEvent: MotionEvent ->
            scaleTouchEvent(
                motionEvent
            )
        })

        deselectSticker()
    }

    private fun removeSticker()
    {
        listener.onRemoveSticker(this)
    }

    private fun mirrorSticker()
    {
        val bitmapDrawable = stickerImage.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        val matrix = Matrix()
        matrix.preScale(-1f, 1f)
        val reflectionBitmap = Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            false
        )
        setPicture(reflectionBitmap)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean
    {
        when (event.actionMasked)
        {
            MotionEvent.ACTION_MOVE ->
            {
                if (event.pointerCount == 2)
                {
                    modeMove = false

                    if (mPtrID1 != INVALID_POINTER_ID && mPtrID2 != INVALID_POINTER_ID)
                    {
                        val nfPoint = PointF()
                        val nsPoint = PointF()

                        getRawPoint(event, mPtrID1, nsPoint, this)
                        getRawPoint(event, mPtrID2, nfPoint, this)

                        mAngle = angleBetweenLines(mFPoint, mSPoint, nfPoint, nsPoint)
                        mAngle = (mAngle + oldRotation!!) % 360

                        stickerImage.pivotX = (width / 2).toFloat()
                        stickerImage.pivotY = (height / 2).toFloat()
                        rotation = mAngle

                    }
                } else if (event.pointerCount == 1 && modeMove)
                {
                    animate()
                        .x(event.rawX + mX)
                        .y(event.rawY + mY)
                        .setDuration(0)
                        .start()
                }
            }

            MotionEvent.ACTION_DOWN ->
            {
                listener.onSelectSticker(this)
                mX = x - event.rawX
                mY = y - event.rawY
                mPtrID1 = event.getPointerId(event.actionIndex)
            }

            MotionEvent.ACTION_POINTER_DOWN ->
            {
                mPtrID2 = event.getPointerId(event.actionIndex)
                getRawPoint(event, mPtrID1, mSPoint, this)
                getRawPoint(event, mPtrID2, mFPoint, this)
                oldRotation = rotation
            }

            MotionEvent.ACTION_UP ->
            {
                modeMove = true
                mPtrID1 = INVALID_POINTER_ID
            }

            MotionEvent.ACTION_POINTER_UP ->
            {
                mPtrID2 = INVALID_POINTER_ID
            }

            MotionEvent.ACTION_CANCEL ->
            {
                mPtrID1 = INVALID_POINTER_ID
                mPtrID2 = INVALID_POINTER_ID
            }
        }

        return true
    }

    private fun scaleTouchEvent(event: MotionEvent): Boolean
    {
        when (event.actionMasked)
        {
            MotionEvent.ACTION_MOVE ->
            {
                nX = event.rawX
                nY = event.rawY
                val dX = nX - sX
                val dY = nY - sY
                val distance = sqrt(dX * dX + dY * dY)
                var newDistance = distance / sqrt(2f)
                newDistance = calculateDistanceFromCurAngle(this, sX, sY, nX, nY, newDistance)

                val newWidth = returnInBoundsSize(currentWidth, newDistance.toInt())
                val newHeight = returnInBoundsSize(currentHeight, newDistance.toInt())

                resizeView(newWidth, newHeight)
            }

            MotionEvent.ACTION_DOWN ->
            {
                currentWidth = width
                currentHeight = height
                sX = event.rawX
                sY = event.rawY
            }


        }
        return true
    }

    private fun getRawPoint(ev: MotionEvent, index: Int, point: PointF, v: View)
    {
        val location = intArrayOf(0, 0)
        v.getLocationOnScreen(location)

        var x = scaleX * ev.getX(index)
        var y = ev.getY(index)
        val angle = Math.toDegrees(atan2(y.toDouble(), x.toDouble())) + v.rotation
        val length = PointF.length(x, y)

        x = ((length * cos(Math.toRadians(angle))) + location[0]).toFloat()
        y = ((length * sin(Math.toRadians(angle))) + location[1]).toFloat()

        point.set(x, y)
    }

    private fun angleBetweenLines(
        fPoint: PointF,
        sPoint: PointF,
        nfPoint: PointF,
        nsPoint: PointF
    ): Float
    {
        val angle1 = atan2(((fPoint.y - sPoint.y).toDouble()), ((fPoint.x - sPoint.x).toDouble()))
        val angle2 = atan2(
            ((nfPoint.y - nsPoint.y).toDouble()),
            ((nfPoint.x - nsPoint.x).toDouble())
        )
        var angle = (Math.toDegrees(angle1 - angle2) % 360).toFloat()

        if (angle < -180f) angle += 360f
        if (angle > 180f) angle -= 360f

        return -angle
    }

    private fun calculateDistanceFromCurAngle(
        view: View,
        sX: Float,
        sY: Float,
        nX: Float,
        nY: Float,
        distance: Float
    ): Float
    {
        var rot = view.rotation
        if (rot < 0) rot += 360f

        var newDistance = distance

        if (rot >= 0 && rot < 90)
        {
            if (nY < sY)
                newDistance *= -1

        } else if (rot >= 90 && rot < 180)
        {
            if (nX > sX)
                newDistance *= -1

        } else if (rot >= 180 && rot < 270)
        {
            if (nY > sY)
                newDistance *= -1

        } else if (rot >= 270 && rot < 360)
        {
            if (nX < sX)
                newDistance *= -1
        }

        return newDistance
    }

    private fun returnInBoundsSize(currentSize: Int, newDistance: Int): Int
    {
        return if (currentSize + newDistance > 100)
            if (currentSize + newDistance < MAX_SIZE)
                currentWidth + newDistance
            else MAX_SIZE
        else
            100
    }

    private fun resizeView(newWidth: Int, newHeight: Int)
    {
        val lp = LayoutParams(newWidth, newHeight)
        val lp2 = RelativeLayout.LayoutParams(newWidth, newHeight)
        lp2.setMargins(25, 25, 25, 25)
        layoutParams = lp
        stickerImage.layoutParams = lp2
        stickerImage.pivotX = (newWidth / 2).toFloat()
        stickerImage.pivotY = (newHeight / 2).toFloat()
    }


    fun deselectSticker()
    {
        stickerImage.setBackgroundColor(Color.TRANSPARENT)
        btnRemove.visibility = View.GONE
        btnMirror.visibility = View.GONE
        btnScale.visibility = View.GONE
    }

    fun selectSticker()
    {
        listener.onDeselectSticker()
        stickerImage.setBackgroundResource(R.drawable.sticker_frame)
        btnRemove.visibility = View.VISIBLE
        btnMirror.visibility = View.VISIBLE
        btnScale.visibility = View.VISIBLE

    }

    fun setPicture(resID: Int)
    {
        Glide.with(context).load(resID).into(stickerImage)
    }

    fun setPicture(uri: Uri)
    {
        Glide.with(context).load(uri).into(stickerImage)
    }

    fun setPicture(bitmap: Bitmap)
    {
        Glide.with(context).load(bitmap).into(stickerImage)
    }

    fun centerView(centerX: Int, centerY: Int)
    {
        val nsW = centerX - width/2
        val nsH = centerY - height/2
        animate()
            .x(nsW.toFloat())
            .y(nsH.toFloat())
            .setDuration(0)
            .start()

    }

}

