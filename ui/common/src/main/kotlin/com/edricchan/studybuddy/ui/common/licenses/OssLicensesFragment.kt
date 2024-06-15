package com.edricchan.studybuddy.ui.common.licenses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.compose.content
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer

@Deprecated("Use the LibrariesContainer composable instead")
class OssLicensesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {
        LibrariesContainer()
    }
}
