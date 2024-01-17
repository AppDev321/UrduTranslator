package com.dictionary.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.android.inputmethod.latin.R

import kotlin.math.max
import kotlin.math.min

class VerticalSlideColorPicker : View {

    private var defaultColor = 0
    private val paint: Paint = Paint()
    private var strokePaint: Paint = Paint()
    private var path: Path = Path()
    private var bitmap: Bitmap? = null
    private var viewWidth = 0
    private var viewHeight = 0
    private var centerX = 0
    private var colorPickerRadius = 0f
    private var colorPickerBody: RectF? = null
    private var selectorYPos = 0f
    private var borderColor = 0
    private var borderWidth = 0f
    private var colors = intArrayOf(0)
    private var cacheBitmap = true
    private var onColorChangeListener: ((selectedColor: Int) -> Unit)? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0) {
        val a = context.theme
            .obtainStyledAttributes(attrs, R.styleable.VerticalSlideColorPicker, 0, 0)
        try {
            borderColor = a.getColor(R.styleable.VerticalSlideColorPicker_borderColor, Color.WHITE)
            defaultColor =
                a.getColor(R.styleable.VerticalSlideColorPicker_defaultColor, Color.TRANSPARENT)
            borderWidth = a.getDimension(R.styleable.VerticalSlideColorPicker_borderWidth, 5f)
            val colorsResourceId =
                a.getResourceId(R.styleable.VerticalSlideColorPicker_colors, R.array.default_colors)
            colors = a.resources.getIntArray(colorsResourceId)
        } finally {
            a.recycle()
        }
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }


    private fun init() {
        setWillNotDraw(false)
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        path = Path()
        strokePaint.style = Paint.Style.STROKE
        strokePaint.color = borderColor
        strokePaint.isAntiAlias = true
        strokePaint.strokeWidth = borderWidth
        isDrawingCacheEnabled = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        path.addCircle(
            centerX.toFloat(),
            borderWidth + colorPickerRadius,
            colorPickerRadius,
            Path.Direction.CW
        )
        colorPickerBody?.let { pickerBody ->
            path.addRect(pickerBody, Path.Direction.CW)
        }
        path.addCircle(
            centerX.toFloat(), viewHeight - (borderWidth + colorPickerRadius), colorPickerRadius,
            Path.Direction.CW
        )
        canvas.drawPath(path, strokePaint)
        canvas.drawPath(path, paint)
        if (cacheBitmap) {
            bitmap = drawingCache
            cacheBitmap = false
            invalidate()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var yPos = min(event.y, colorPickerBody?.bottom ?: 0f)
        yPos = max(colorPickerBody?.top ?: 0f, yPos)
        selectorYPos = yPos
        defaultColor = bitmap?.getPixel(viewWidth / 2, selectorYPos.toInt())
            ?: context.getColor(R.color.colorPrimary)
        onColorChangeListener?.invoke(defaultColor)
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
        centerX = viewWidth / 2
        colorPickerRadius = viewWidth / 2 - borderWidth
        colorPickerBody = RectF(
            centerX - colorPickerRadius, borderWidth + colorPickerRadius,
            centerX + colorPickerRadius, viewHeight - (borderWidth + colorPickerRadius)
        )
        val gradient = LinearGradient(
            0f,
            colorPickerBody?.top ?: 0f,
            0f,
            colorPickerBody?.bottom ?: 0f,
            colors,
            null,
            Shader.TileMode.CLAMP
        )
        paint.shader = gradient
        resetToDefault()
    }

    private fun resetToDefault() {
        selectorYPos = borderWidth + colorPickerRadius
        onColorChangeListener?.invoke(defaultColor)
        invalidate()
    }

    fun setOnColorChangeListener(onColorChangeListener: ((selectedColor: Int) -> Unit)? = null) {
        this.onColorChangeListener = onColorChangeListener
        onColorChangeListener?.invoke(defaultColor)
    }

}