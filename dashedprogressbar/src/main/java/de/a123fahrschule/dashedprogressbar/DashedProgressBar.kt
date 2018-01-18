package de.a123fahrschule.dashedprogressbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

open class DashedProgressBar : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        if (attrs != null) {
            initAttrs(attrs)
        }
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (attrs != null) {
            initAttrs(attrs)
        }
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        if (attrs != null) {
            initAttrs(attrs)
        }
    }

    private var filledProgressBarLength: Float = 0.0f
    private var filledProgressBarWidth: Float = 0.0f
    private var filledProgressBarSpacing: Float = 0.0f

    private var emptyProgressBarLength: Float = 0.0f
    private var emptyProgressBarWidth: Float = 0.0f
    private var emptyProgressBarSpacing: Float = 0.0f

    private var emptyColor: Int = 0
    private var fillColor: Int = 0

    private var startAngle: Float = 0f
    private var endAngle: Float = 0f

    var progess: Float = 0.5f
        set(value) {
            field = value
            invalidate()
        }

    private val emptyBarPAint by lazy {
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.strokeWidth = emptyProgressBarWidth
        p.style = Paint.Style.STROKE
        p.color = emptyColor
        p.pathEffect = DashPathEffect(FloatArray(2, { a ->
            when (a) {
                1 -> emptyProgressBarSpacing
                else -> emptyProgressBarLength
            }
        }), 0f)
        return@lazy p
    }

    private val filledBarPaint by lazy {
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.strokeWidth = filledProgressBarWidth
        p.style = Paint.Style.STROKE
        p.color = fillColor
        p.pathEffect = DashPathEffect(FloatArray(2, { a ->
            when (a) {
                1 -> filledProgressBarSpacing
                else -> filledProgressBarLength
            }
        }), 0f)
        return@lazy p
    }

    private fun initAttrs(attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.DashedProgressBar, 0, 0)
        try {
            filledProgressBarLength = ta.getDimension(R.styleable.DashedProgressBar_filledProgressBarLength, 0f)
            filledProgressBarWidth = ta.getDimension(R.styleable.DashedProgressBar_filledProgressBarWidth, 0f)
            filledProgressBarSpacing = ta.getDimension(R.styleable.DashedProgressBar_filledProgressBarSpacing, 0f)
            fillColor = ta.getColor(R.styleable.DashedProgressBar_filledProgressBarColor, 0)

            emptyProgressBarLength = ta.getDimension(R.styleable.DashedProgressBar_emptyProgressBarLength, 0f)
            emptyProgressBarWidth = ta.getDimension(R.styleable.DashedProgressBar_emptyProgressBarWidth, 0f)
            emptyProgressBarSpacing = ta.getDimension(R.styleable.DashedProgressBar_emptyProgressBarSpacing, 0f)
            emptyColor = ta.getColor(R.styleable.DashedProgressBar_emptyProgressBarColor, 0)

            startAngle = ta.getFloat(R.styleable.DashedProgressBar_startAngle, 0f)
            endAngle = ta.getFloat(R.styleable.DashedProgressBar_endAngle, 360f)

        } finally {
            ta.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        //radius = Math.min(canvas.width, canvas.height).toFloat()

        super.onDraw(canvas)
        val padding = Math.max(emptyProgressBarWidth, filledProgressBarWidth) / 2
        val h = canvas.height - padding
        val w = canvas.width - padding

        val pad = Math.min(getPaddingHeight(canvas.height.toFloat() / 2f, -1 * startAngle), getPaddingHeight(canvas.height.toFloat() / 2f, -1 * endAngle))
        val totalAngle = 360 - startAngle + endAngle
        canvas.drawArc(padding, padding + pad, w, h + pad, endAngle, -1 * (totalAngle - (totalAngle * progess)), false, emptyBarPAint)
        canvas.drawArc(padding, padding + pad, w, h + pad, startAngle, totalAngle * progess, false, filledBarPaint)

    }

    private fun getPaddingHeight(radius: Float, angle: Float): Float {
        return (Math.sin(Math.toRadians(angle.toDouble())) * radius + radius).toFloat()
    }

}