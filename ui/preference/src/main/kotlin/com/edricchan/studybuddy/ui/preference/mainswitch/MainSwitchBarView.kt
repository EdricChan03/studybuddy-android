package com.edricchan.studybuddy.ui.preference.mainswitch

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.Checkable
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.AbstractComposeView
import com.edricchan.studybuddy.ui.preference.compose.MainSwitchBar
import com.edricchan.studybuddy.ui.preference.compose.MainSwitchBarDefaults
import com.edricchan.studybuddy.ui.preference.mainswitch.MainSwitchBarView.SavedState
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import kotlinx.parcelize.Parcelize

/**
 * Wrapper [android.view.View] for the [MainSwitchBar] composable.
 *
 * Note: `null` is passed as the `switchView` argument for [OnMainSwitchChangeListener]s
 * passed to [addOnCheckedChangeListener].
 */
@Deprecated("Use MainSwitchBar directly where possible")
class MainSwitchBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbstractComposeView(context, attrs, defStyleAttr), Checkable {
    init {
        isClickable = true
        isFocusable = true
    }

    private val checkedChangeListeners = mutableListOf<OnMainSwitchChangeListener>()

    /**
     * Adds a [OnMainSwitchChangeListener] which will be invoked
     * when the switch bar's checked state is updated.
     *
     * **Note:** The `switchView` argument for [OnMainSwitchChangeListener.onSwitchChanged]
     * will be `null` when invoked.
     */
    fun addOnCheckedChangeListener(listener: OnMainSwitchChangeListener) {
        checkedChangeListeners += listener
    }

    /**
     * Removes the specified [OnMainSwitchChangeListener] from the list
     * of [OnMainSwitchChangeListener]s.
     */
    fun removeOnCheckedChangeListener(listener: OnMainSwitchChangeListener) {
        checkedChangeListeners -= listener
    }

    private var isCheckedState by mutableStateOf(false)

    override fun isChecked(): Boolean = isCheckedState
    override fun setChecked(checked: Boolean) {
        isCheckedState = checked
    }

    override fun toggle() {
        isCheckedState = !isCheckedState
    }

    private var isEnabledState by mutableStateOf(true)

    override fun isEnabled(): Boolean = isEnabledState
    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        isEnabledState = enabled
    }

    /** The switch bar's text, as a [String]. */
    var title by mutableStateOf("")

    /** Sets the switch bar's text from the given [text resource][textRes]. */
    fun setTitle(@StringRes textRes: Int) {
        title = context.getString(textRes)
    }

    private fun onCheckedChange(newChecked: Boolean) {
        isCheckedState = newChecked
        checkedChangeListeners.forEach { it.onSwitchChanged(null, isCheckedState) }
    }

    @Composable
    override fun Content() {
        StudyBuddyTheme {
            MainSwitchBar(
                checked = isCheckedState,
                onCheckedChange = ::onCheckedChange,
                enabled = isEnabledState,
                text = { MainSwitchBarDefaults.TitleText(text = title) }
            )
        }
    }

    @VisibleForTesting
    @Parcelize
    internal data class SavedState(
        val superState: Parcelable?,
        val title: String, val checked: Boolean, val enabled: Boolean
    ) : Parcelable

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as? SavedState
        super.onRestoreInstanceState(savedState?.superState)

        savedState?.let(::restoreState)
    }

    override fun onSaveInstanceState(): Parcelable = saveState(
        super.onSaveInstanceState()
    )
}

@VisibleForTesting
internal fun MainSwitchBarView.restoreState(state: SavedState) {
    title = state.title
    isChecked = state.checked
    isEnabled = state.enabled
}

@VisibleForTesting
internal fun MainSwitchBarView.saveState(superState: Parcelable?): SavedState = SavedState(
    superState = superState,
    title = title,
    checked = isChecked,
    enabled = isEnabled
)
