package com.jampez.multistatetogglebutton

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

@Suppress("unused")
open class ToggleButton : LinearLayout {
    interface OnValueChangedListener {
        // public void onValueChanged(int value, boolean selected);
        fun onValueChanged(value: Int)
    }

    var listener: OnValueChangedListener? = null

    @ColorInt
    var colorPressed = 0

    @ColorInt
    var colorNotPressed = 0

    @ColorInt
    var borderColor // Resolved colors (format 0xAARRGGBB)
            = 0
    var colorPressedText = 0
    var colorPressedBackground = 0
    var colorNotPressedText = 0
    var colorNotPressedBackground = 0
    var pressedBackgroundResource = 0
    var notPressedBackgroundResource = 0

    constructor(ctx: Context) : super(ctx, null)

    constructor(ctx: Context, attrs: AttributeSet?) : super(ctx, attrs)

    fun setOnValueChangedListener(l: OnValueChangedListener?) {
        listener = l
    }

    open fun setValue(value: Int) {
        if (listener != null) listener!!.onValueChanged(value)
    }

    /**
     * The desired color resource identifier generated by the aapt tool
     *
     * @param colorPressed    color resource ID for the pressed button(s)
     * @param colorNotPressed color resource ID for the released button(s)
     */
    fun setColorRes(@ColorRes colorPressed: Int, @ColorRes colorNotPressed: Int, @ColorRes borerColor: Int) {
        setColors(ContextCompat.getColor(context, colorPressed), ContextCompat.getColor(context, colorNotPressed), ContextCompat.getColor(context, borerColor))
    }

    /**
     * Color values are in the form 0xAARRGGBB
     *
     * @param colorPressed    resolved color for the pressed button(s)
     * @param colorNotPressed resolved color for the released button(s)
     * @param borderColor resolved color for the released button(s)
     */
    open fun setColors(@ColorInt colorPressed: Int, @ColorInt colorNotPressed: Int, @ColorInt borderColor: Int) {
        this.colorPressed = colorPressed
        this.colorNotPressed = colorNotPressed
        this.borderColor = borderColor
    }

    /**
     * The desired color resource identifier generated by the aapt tool
     *
     * @param colorPressedText  color resource ID for the pressed button's text
     * @param colorPressedBackground  color resource ID for the pressed button's background
     */
    fun setPressedColorsRes(@ColorRes colorPressedText: Int, @ColorRes colorPressedBackground: Int) {
        setPressedColors(ContextCompat.getColor(context, colorPressedText), ContextCompat.getColor(context, colorPressedBackground))
    }

    /**
     * Color values are in the form 0xAARRGGBB
     *
     * @param colorPressedText  resolved color for the pressed button's text
     * @param colorPressedBackground  resolved color for the pressed button's background
     */
    fun setPressedColors(@ColorInt colorPressedText: Int, @ColorInt colorPressedBackground: Int) {
        this.colorPressedText = colorPressedText
        this.colorPressedBackground = colorPressedBackground
    }

    /**
     * The desired color resource identifier generated by the aapt tool
     *
     * @param colorNotPressedText  color resource ID for the released button's text
     * @param colorNotPressedBackground  color resource ID for the released button's background
     */
    fun setNotPressedColorRes(@ColorRes colorNotPressedText: Int, @ColorRes colorNotPressedBackground: Int) {
        setNotPressedColors(ContextCompat.getColor(context, colorNotPressedText), ContextCompat.getColor(context, colorNotPressedBackground))
    }

    /**
     * Color values are in the form 0xAARRGGBB
     *
     * @param colorNotPressedText  resolved color for the released button's text
     * @param colorNotPressedBackground  resolved color for the released button's background
     */
    fun setNotPressedColors(colorNotPressedText: Int, colorNotPressedBackground: Int) {
        this.colorNotPressedText = colorNotPressedText
        this.colorNotPressedBackground = colorNotPressedBackground
    }

    /**
     * The desired color resource identifier generated by the aapt tool
     *
     * @param pressedBackgroundResource     drawable resource ID for the pressed button's background
     * @param notPressedBackgroundResource  drawable resource ID for the released button's background
     */
    fun setBackgroundResources(@DrawableRes pressedBackgroundResource: Int, @DrawableRes notPressedBackgroundResource: Int) {
        this.pressedBackgroundResource = pressedBackgroundResource
        this.notPressedBackgroundResource = notPressedBackgroundResource
    }

    /**
     * The desired color resource identifier generated by the aapt tool
     *
     * @param colorPressedText     drawable resource ID for the pressed button's background
     * @param colorNotPressedText  drawable resource ID for the released button's background
     */
    fun setForegroundColorsRes(@ColorRes colorPressedText: Int, @ColorRes colorNotPressedText: Int) {
        setForegroundColors(ContextCompat.getColor(context, colorPressedText), ContextCompat.getColor(context, colorNotPressedText))
    }

    /**
     * Color values are in the form 0xAARRGGBB
     *
     */
    fun setForegroundColors(colorPressedText: Int, colorNotPressedText: Int) {
        this.colorPressedText = colorPressedText
        this.colorNotPressedText = colorNotPressedText
    }
}