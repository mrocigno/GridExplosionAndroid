package br.com.mrocigno.gridexplosion

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.animation.doOnStart
import androidx.core.graphics.ColorUtils
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import java.util.*
import kotlin.math.roundToInt

class ExplosionLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var pixelSize = 50f

    private var mWidth = 0
    private var mHeight = 0

    private var pixels: List<List<PixelModel>> = emptyList()

    private var currentColor = 0
    var colors = listOf(
        Color.RED,
        Color.BLUE,
        Color.YELLOW,
        Color.GREEN,
        Color.CYAN,
        Color.MAGENTA,
        Color.WHITE,
        Color.BLACK
    )

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (changed) {
            val numLines = (mHeight / pixelSize).roundToInt()
            val numColumns = (mWidth / pixelSize).roundToInt()
            pixels = List(numLines) { line ->
                List(numColumns) { column ->
                    PixelModel(line, column)
                }
            }

        }
    }

    fun startAnimation(event: MotionEvent) {
        val x = (event.x / pixelSize).roundToInt()
        val y = (event.y / pixelSize).roundToInt()

        startAnimation(x to y)
    }

    fun startAnimation(center: Pair<Int, Int>) {
        val color = colors.getOrNull(currentColor++) ?: let {
            currentColor = 1
            colors.first()
        }
        ValueAnimator.ofInt(0, 50).apply {
            duration = 1000L
            addUpdateListener {
                val value = it.animatedValue as Int
                DrawUtils.midPointCircleDraw(center, value).forEach { point ->
                    val (line, min, max) = point
                    pixels.getOrNull(line)?.forEach { pixel ->
                        if (pixel.column in min..max) {
                            pixel.color = color
                        }
                    }
                }
                invalidate()
            }
        }.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            pixels.forEach {
                it.forEach { pixel ->
                    val left = pixel.column * pixelSize
                    val top = pixel.line * pixelSize
                    paint.color = pixel.color
                    paint.style = Paint.Style.FILL
                    drawRect(left, top, left + pixelSize, top + pixelSize, paint)
                    paint.color = ColorUtils.blendARGB(pixel.color, Color.BLACK, .2f)
                    paint.style = Paint.Style.STROKE
                    drawRect(left, top, left + pixelSize, top + pixelSize, paint)
                }
            }
        }
    }
}

class PixelModel(
    val line: Int,
    val column: Int,
    @ColorInt var color: Int = Color.TRANSPARENT
)