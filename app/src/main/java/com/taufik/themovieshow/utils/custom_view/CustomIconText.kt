package com.taufik.themovieshow.utils.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.taufik.themovieshow.R

class IconTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val icon: ImageView
    private val text: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_icon_text, this, true)
        icon = findViewById(R.id.imgIcon)
        text = findViewById(R.id.tvText)

        context.theme.obtainStyledAttributes(attrs, R.styleable.IconTextView, 0, 0).apply {
            try {
                val iconRes = getResourceId(R.styleable.IconTextView_iconSrc, 0)
                if (iconRes != 0) icon.setImageResource(iconRes)

                val label = getString(R.styleable.IconTextView_text)
                text.text = label ?: ""

                val padding = getDimensionPixelSize(R.styleable.IconTextView_contentPadding, -1)
                if (padding >= 0) setPadding(padding, padding, padding, padding)

            } finally {
                recycle()
            }
        }
    }

    fun setText(value: String) {
        text.text = value
    }

    fun setTextSize(size: Float) {
        text.textSize = size
    }

    fun Context.setTextColor(@ColorRes color: Int) {
        text.setTextColor(ContextCompat.getColor(this, color))
    }

    fun setIcon(@DrawableRes resId: Int) {
        icon.setImageResource(resId)
    }

    fun Context.setIconColor(@ColorRes color: Int) {
        icon.setColorFilter(ContextCompat.getColor(this, color))
    }
}
