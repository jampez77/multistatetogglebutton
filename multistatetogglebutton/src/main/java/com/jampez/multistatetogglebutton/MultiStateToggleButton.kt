package com.jampez.multistatetogglebutton

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.Gravity.CENTER_VERTICAL
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.widget.TextViewCompat
import java.util.*
import kotlin.math.max

@Suppress("unused", "MemberVisibilityCanBePrivate")
class MultiStateToggleButton : ToggleButton {
    /**
     * A list of rendered buttons. Used to get state, among others
     */
    lateinit var buttons: MutableList<View>

    /**
     * @return An array of the buttons' text
     */
    /**
     * The specified texts
     */
    var texts: Array<String?>? = null

    /**
     * If true, multiple buttons can be pressed at the same time
     */
    var mMultipleChoice = false

    /**
     * The layout containing all buttons
     */
    private var mainLayout: LinearLayout? = null

    constructor(ctx: Context) : super(ctx, null)
    constructor(ctx: Context, attrs: AttributeSet?) : super(ctx, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MultiStateToggleButton, 0, 0)
        try {
            val texts = a.getTextArray(R.styleable.MultiStateToggleButton_values)
            colorPressed = a.getColor(R.styleable.MultiStateToggleButton_mstbPrimaryColor, 0)
            colorNotPressed = a.getColor(R.styleable.MultiStateToggleButton_mstbSecondaryColor, 0)
            borderColor = a.getColor(R.styleable.MultiStateToggleButton_mstbBorderColor, 0)
            colorPressedText = a.getColor(R.styleable.MultiStateToggleButton_mstbColorPressedText, 0)
            colorPressedBackground = a.getColor(R.styleable.MultiStateToggleButton_mstbColorPressedBackground, 0)
            pressedBackgroundResource = a.getResourceId(R.styleable.MultiStateToggleButton_mstbColorPressedBackgroundResource, 0)
            colorNotPressedText = a.getColor(R.styleable.MultiStateToggleButton_mstbColorNotPressedText, 0)
            colorNotPressedBackground = a.getColor(R.styleable.MultiStateToggleButton_mstbColorNotPressedBackground, 0)
            notPressedBackgroundResource = a.getResourceId(R.styleable.MultiStateToggleButton_mstbColorNotPressedBackgroundResource, 0)
            var length = 0
            if (texts != null) {
                length = texts.size
            }
            val transformArray = arrayListOf<String?>()
            texts.forEach {
                transformArray.add(it.toString())
            }
            setElements(transformArray.toTypedArray(), null, BooleanArray(length))
        } finally {
            a.recycle()
        }
    }

    /**
     * If multiple choice is enabled, the user can select multiple
     * values simultaneously.
     *
     */
    fun enableMultipleChoice(enable: Boolean) {
        mMultipleChoice = enable
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(KEY_INSTANCE_STATE, super.onSaveInstanceState())
        bundle.putBooleanArray(KEY_BUTTON_STATES, states)
        return bundle
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        var parcelableState: Parcelable? = state
        if (parcelableState is Bundle) {
            val bundle = parcelableState
            states = bundle.getBooleanArray(KEY_BUTTON_STATES)
            parcelableState = bundle.getParcelable(KEY_INSTANCE_STATE)
        }
        super.onRestoreInstanceState(parcelableState)
    }

    /**
     * Set the enabled state of this MultiStateToggleButton, including all of its child buttons.
     *
     * @param enabled True if this view is enabled, false otherwise.
     */
    override fun setEnabled(enabled: Boolean) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.isEnabled = enabled
        }
    }

    /**
     * Set multiple buttons with the specified texts and default
     * initial values. Initial states are allowed, but both
     * arrays must be of the same size.
     *
     * @param texts            An array of CharSequences for the buttons
     * @param imageResourceIds an optional icon to show, either text, icon or both needs to be set.
     * @param selected         The default value for the buttons
     */
    fun setElements(texts: Array<String?>, imageResourceIds: IntArray?, selected: BooleanArray?) {
        this.texts = texts
        val textCount = texts.size
        val iconCount = imageResourceIds?.size ?: 0
        val elementCount = max(textCount, iconCount)
        if (elementCount == 0) return
        var enableDefaultSelection = true
        if (selected == null || elementCount != selected.size) enableDefaultSelection = false
        orientation = HORIZONTAL
        gravity = CENTER_VERTICAL
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (mainLayout == null) mainLayout = inflater.inflate(R.layout.view_multi_state_toggle_button, this, true) as LinearLayout
        mainLayout!!.removeAllViews()
        buttons = ArrayList(elementCount)
        for (i in 0 until elementCount) {
            val b: Button = if (i == 0) {
                // Add a special view when there's only one element
                if (elementCount == 1) inflater.inflate(R.layout.view_single_toggle_button, mainLayout, false) as Button else inflater.inflate(R.layout.view_left_toggle_button, mainLayout, false) as Button
            } else if (i == elementCount - 1) inflater.inflate(R.layout.view_right_toggle_button, mainLayout, false) as Button else inflater.inflate(R.layout.view_center_toggle_button, mainLayout, false) as Button
            b.text = texts[i]
            if (imageResourceIds != null && imageResourceIds[i] != 0) b.setCompoundDrawablesWithIntrinsicBounds(imageResourceIds[i], 0, 0, 0)
            b.setOnClickListener { setValue(i) }
            mainLayout!!.addView(b)
            if (enableDefaultSelection) setButtonState(b, selected!![i])
            buttons.add(b)
        }
        mainLayout!!.setBackgroundResource(R.drawable.button_section_shape)
        val border = GradientDrawable()
        border.setStroke(4, borderColor) //border with full opacity
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
            mainLayout!!.setBackgroundDrawable(border)
        else mainLayout!!.background = border
    }

    /**
     * Set multiple buttons with the specified texts and default
     * initial values. Initial states are allowed, but both
     * arrays must be of the same size.
     *
     * @param buttons  the array of button views to use
     * @param selected The default value for the buttons
     */
    fun setButtons(buttons: Array<View>, selected: BooleanArray?) {
        val elementCount = buttons.size
        if (elementCount == 0) return
        var enableDefaultSelection = true
        if (selected == null || elementCount != selected.size) enableDefaultSelection = false
        orientation = HORIZONTAL
        gravity = CENTER_VERTICAL
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (mainLayout == null) mainLayout = inflater.inflate(R.layout.view_multi_state_toggle_button, this, true) as LinearLayout
        mainLayout!!.removeAllViews()
        this.buttons = ArrayList()
        for (i in 0 until elementCount) {
            val b = buttons[i]
            b.setOnClickListener { setValue(i) }
            mainLayout!!.addView(b)
            if (enableDefaultSelection) setButtonState(b, selected!![i])
            this.buttons.add(b)
        }
        mainLayout!!.setBackgroundResource(R.drawable.button_section_shape)
    }

    fun setElements(elements: Array<String?>) {
        val size = elements.size
        setElements(elements, null, BooleanArray(size))
    }

    fun setElements(elements: List<*>?) {
        val size = elements?.size ?: 0
        setElements(elements, BooleanArray(size))
    }

    fun setElements(elements: List<*>?, selected: Any?) {
        var size = 0
        var index = -1
        if (elements != null) {
            size = elements.size
            index = elements.indexOf(selected)
        }
        val selectedArray = BooleanArray(size)
        if (index != -1 && index < size) selectedArray[index] = true
        setElements(elements, selectedArray)
    }

    fun setElements(texts: List<*>?, selected: BooleanArray?) {
        var mutableTexts = texts
        if (mutableTexts == null) mutableTexts = arrayListOf(0)
        val size = mutableTexts.size
        setElements(arrayOfNulls(size), null, selected)
    }

    fun setElements(arrayResourceId: Int, selectedPosition: Int) {
        // Get resources
        val elements = this.resources.getStringArray(arrayResourceId)

        // Set selected boolean array
        val size = elements.size
        val selected = BooleanArray(size)
        if (selectedPosition in 0 until size) selected[selectedPosition] = true
        // Super
        setElements(elements, null, selected)
    }

    fun setElements(arrayResourceId: Int, selected: BooleanArray?) {
        setElements(this.resources.getStringArray(arrayResourceId), null, selected)
    }

    fun setButtonState(button: View?, selected: Boolean) {
        if (button == null) return
        button.isSelected = selected
        button.setBackgroundResource(if (selected) R.drawable.button_pressed else R.drawable.button_not_pressed)
        if (colorPressed != 0 || colorNotPressed != 0) button.setBackgroundColor(if (selected) colorPressed else colorNotPressed) else if (colorPressedBackground != 0 || colorNotPressedBackground != 0) button.setBackgroundColor(if (selected) colorPressedBackground else colorNotPressedBackground)
        if (button is Button) {
            val style = if (selected) R.style.WhiteBoldText else R.style.PrimaryNormalText
            TextViewCompat.setTextAppearance(button, style)
            if (colorPressed != 0 || colorNotPressed != 0) button.setTextColor(if (!selected) colorPressed else colorNotPressed)
            if (colorPressedText != 0 || colorNotPressedText != 0) button.setTextColor(if (selected) colorPressedText else colorNotPressedText)
            if (pressedBackgroundResource != 0 || notPressedBackgroundResource != 0) button.setBackgroundResource(if (selected) pressedBackgroundResource else notPressedBackgroundResource)
        }
    }

    fun getValue(): Int {
        for (i in buttons.indices) {
            if (buttons[i].isSelected) return i
        }
        return -1
    }

    override fun setValue(value: Int) {
        for (i in buttons.indices) {
            if (mMultipleChoice) {
                if (i == value) {
                    val b = buttons[i]
                    setButtonState(b, !b.isSelected)
                }
            } else {
                if (i == value) setButtonState(buttons[i], true) else setButtonState(buttons[i], false)
            }
        }
        super.setValue(value)
    }

    var states: BooleanArray?
        get() {
            val size = buttons.size
            val result = BooleanArray(size)
            for (i in 0 until size) result[i] = buttons[i].isSelected
            return result
        }
        set(selected) {
            if (selected == null || buttons.size != selected.size) return
            for ((count, b) in buttons.withIndex()) {
                setButtonState(b, selected[count])
            }
        }

    /**
     * {@inheritDoc}
     */
    override fun setColors(colorPressed: Int, colorNotPressed: Int, borderColor: Int) {
        super.setColors(colorPressed, colorNotPressed, borderColor)
        refresh()
    }

    private fun refresh() {
        val states = states!!
        for (i in states.indices) setButtonState(buttons[i], states[i])
    }

    companion object {
        private val TAG = MultiStateToggleButton::class.java.simpleName
        private const val KEY_BUTTON_STATES = "button_states"
        private const val KEY_INSTANCE_STATE = "instance_state"
    }
}