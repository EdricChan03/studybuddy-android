package com.edricchan.studybuddy.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.children
import androidx.core.view.minusAssign
import androidx.core.view.plusAssign
import androidx.core.widget.doAfterTextChanged
import com.edricchan.studybuddy.ui.widget.databinding.ViewChipsTextInputLayoutBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputLayout

class ChipsTextInputLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    private var binding: ViewChipsTextInputLayoutBinding

    /**
     * The internal [TextInputLayout]
     */
    var textInputLayout: TextInputLayout
        private set

    /**
     * The internal [ChipGroup]
     */
    var chipGroup: ChipGroup
        private set

    init {
        binding =
            ViewChipsTextInputLayoutBinding.inflate(LayoutInflater.from(context), this, true).also {
                textInputLayout = it.textInputLayout
                chipGroup = it.chipGroup
            }

        context.withStyledAttributes(attrs, R.styleable.ChipsTextInputLayout) {
            textInputLayout.apply {
                hint = getString(R.styleable.ChipsTextInputLayout_textInputLayoutHint)
                    ?: context.getString(R.string.chips_text_input_layout_default_hint)
                helperText = getString(R.styleable.ChipsTextInputLayout_textInputLayoutHelperText)
            }
        }

        textInputLayout.editText?.apply {
            doAfterTextChanged { editable ->
                val trimmed = editable?.toString().orEmpty().trim()
                if (trimmed.length > 1 && trimmed.endsWith(",")) {
                    chipGroup += Chip(context).apply {
                        text = trimmed.substring(0, trimmed.length - 1)
                        setOnCloseIconClickListener {
                            chipGroup -= this
                        }
                    }
                    editable?.clear()
                }
            }

            setOnKeyListener { _, _, event ->
                if (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_DEL) {
                    if (text.isNullOrEmpty() && chipGroup.childCount > 0) {
                        chipGroup -= chipGroup.children.last() as Chip
                    }
                }
                false
            }
        }
    }
}
