package com.edricchan.studybuddy.ui.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.getSystemService
import com.edricchan.studybuddy.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ChipsTextInputLayout : LinearLayout {
    /**
     * The internal [TextInputLayout]
     */
    var textInputLayout: TextInputLayout?
    /**
     * The internal [TextInputEditText]
     */
    var textInputEditText: TextInputEditText?
    /**
     * The internal [ChipGroup]
     */
    var chipGroup: ChipGroup?

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val styledAttrs =
            context.obtainStyledAttributes(attrs, R.styleable.ChipsTextInputLayout, 0, 0)
        val textInputLayoutHint =
            styledAttrs.getString(R.styleable.ChipsTextInputLayout_textInputLayoutHint)
                ?: context.getString(R.string.chips_text_input_layout_default_hint)
        val textInputLayoutHelperText =
            styledAttrs.getString(R.styleable.ChipsTextInputLayout_textInputLayoutHelperText)

        val inflater = context.getSystemService<LayoutInflater>()
        val view = inflater?.inflate(R.layout.view_chips_text_input_layout, this, true)

        textInputLayout = view?.findViewById(R.id.textInputLayout)
        textInputEditText = view?.findViewById(R.id.textInputEditText)
        chipGroup = view?.findViewById(R.id.chipGroup)

        textInputLayout?.apply {
            hint = textInputLayoutHint
            helperText = textInputLayoutHelperText
        }

        textInputEditText?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val trimmed = editable.toString().trim { it <= ' ' }
                if (trimmed.length > 1 && trimmed.endsWith(",")) {
                    val chip = Chip(context)
                    chip.text = trimmed.substring(0, trimmed.length - 1)
                    chip.isCloseIconVisible = true

                    //Callback fired when chip close icon is clicked
                    chip.setOnCloseIconClickListener {
                        chipGroup?.removeView(chip)
                    }

                    chipGroup?.addView(chip)
                    editable?.clear()
                }
            }
        })
        textInputEditText?.setOnKeyListener { _, _, event ->
            if (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_DEL) {
                if (textInputEditText?.length() == 0 && chipGroup?.childCount!! > 0) {
                    val chip =
                        chipGroup?.childCount?.minus(1)?.let { chipGroup?.getChildAt(it) } as Chip
                    chipGroup?.removeView(chip)
                }
            }
            false
        }
    }
}