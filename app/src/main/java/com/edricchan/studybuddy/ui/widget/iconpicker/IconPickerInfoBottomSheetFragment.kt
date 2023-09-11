package com.edricchan.studybuddy.ui.widget.iconpicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.databinding.FragIconInfoBottomSheetBinding
import com.edricchan.studybuddy.exts.firebase.format
import com.edricchan.studybuddy.interfaces.chat.icon.ChatIcon
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Bottom sheet to show the given [icon]'s metadata.
 * @property icon The icon data to show.
 */
class IconPickerInfoBottomSheetFragment(
    var icon: ChatIcon? = null
) : BottomSheetDialogFragment() {
    private lateinit var binding: FragIconInfoBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        FragIconInfoBottomSheetBinding.inflate(inflater, container, false)
            .also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        icon?.let {
            binding.apply {
                iconNameValue.text = it.name
                iconDescValue.text = it.description
                iconAuthorValue.text = it.author
                iconAboutAuthorValue.text = it.authorInfo
                iconDateCreatedAtValue.text =
                    it.createdAt?.format(getString(R.string.date_format_pattern))
                iconDateLastModifiedValue.text =
                    it.lastModified?.format(getString(R.string.date_format_pattern))
            }
        }
    }
}
