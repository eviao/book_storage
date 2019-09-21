package cn.eviao.bookstorage.drawable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.TypedValue
import cn.eviao.bookstorage.R


class TextDrawable(context: Context, val text: String) : Drawable() {

    private var textPaint: TextPaint

    init {
        val resources = context.resources

        val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        textPaint.density = resources.displayMetrics.density
        textPaint.isDither = true
        textPaint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
            20.0f, resources.displayMetrics)
        textPaint.color = context.getColor(R.color.colorPrimary)

        this.textPaint = textPaint
    }

    override fun setAlpha(alpha: Int) {
        if (textPaint.alpha != alpha) {
            textPaint.alpha = alpha
        }
    }

    override fun getOpacity(): Int {
        return textPaint.alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        if (textPaint.colorFilter != colorFilter) {
            textPaint.colorFilter = colorFilter
        }
    }

    override fun draw(canvas: Canvas) {
        val textLayout = StaticLayout.Builder.obtain(text, 0, text.length, textPaint, 320)
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .setIncludePad(false)
            .setLineSpacing(0.0f, 1.0f).build()

        val restoreCount = canvas.save()

        canvas.translate(bounds.left.toFloat(), bounds.top.toFloat())
        textLayout.draw(canvas)
        canvas.restoreToCount(restoreCount)
    }
}