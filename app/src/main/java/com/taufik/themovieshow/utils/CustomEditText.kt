package com.taufik.themovieshow.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.taufik.themovieshow.R

class CustomEditText: AppCompatEditText, View.OnTouchListener {

    private lateinit var searchIcon: Drawable
    private lateinit var clearIcon: Drawable

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        searchIcon = ContextCompat.getDrawable(context, R.drawable.ic_search) as Drawable
        clearIcon = ContextCompat.getDrawable(context, R.drawable.ic_outline_clear) as Drawable

        setOnTouchListener(this)

        when (id) {
            R.id.etSearch -> {
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (p0.toString().isNotEmpty()) showClearButton() else hideClearButton()
                    }

                    override fun afterTextChanged(p0: Editable?) {}
                })
            }
        }
    }

    private fun showClearButton() {
        when (id) {
            R.id.etSearch -> setButtonDrawables(startOfTheText = searchIcon, endOfTheText = clearIcon)
        }
    }

    private fun hideClearButton() {
        when (id) {
            R.id.etSearch -> setButtonDrawables(startOfTheText = searchIcon)
        }
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (view.id) {
            R.id.etSearch -> {
                if (compoundDrawables[2] != null) {
                    val clearButtonStart: Float
                    val clearButtonEnd: Float
                    var isClearButtonClicked = false
                    if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                        clearButtonEnd = (clearIcon.intrinsicWidth + paddingStart).toFloat()
                        when {
                            event.x < clearButtonEnd -> isClearButtonClicked = true
                        }
                    } else {
                        clearButtonStart = (width - paddingEnd - clearIcon.intrinsicWidth).toFloat()
                        when {
                            event.x > clearButtonStart -> isClearButtonClicked = true
                        }
                    }

                    if (isClearButtonClicked) {
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                searchIcon = ContextCompat.getDrawable(context, R.drawable.ic_search) as Drawable
                                clearIcon = ContextCompat.getDrawable(context, R.drawable.ic_outline_clear) as Drawable
                                showClearButton()
                                return true
                            }

                            MotionEvent.ACTION_UP -> {
                                searchIcon = ContextCompat.getDrawable(context, R.drawable.ic_search) as Drawable
                                clearIcon = ContextCompat.getDrawable(context, R.drawable.ic_outline_clear) as Drawable
                                when {
                                    text != null -> text?.clear()
                                }

                                hideClearButton()
                                return true
                            }
                            else -> return false
                        }
                    } else return false
                }
            }
            else -> return false
        }

        return false
    }
}