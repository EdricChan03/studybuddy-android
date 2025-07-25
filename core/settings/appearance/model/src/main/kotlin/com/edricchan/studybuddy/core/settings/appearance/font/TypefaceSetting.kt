package com.edricchan.studybuddy.core.settings.appearance.font

import androidx.annotation.StringRes
import com.edricchan.studybuddy.core.settings.appearance.proto.TypefaceSettingProto
import com.edricchan.studybuddy.core.settings.appearance.resources.R

enum class TypefaceSetting(
    val protoValue: TypefaceSettingProto,
    val category: Category
) {
    /** Use the [Baloo 2 font](https://fonts.google.com/specimen/Baloo+2). */
    Baloo2(
        protoValue = TypefaceSettingProto.Baloo2,
        category = Category.Default
    ),

    /** Use the [Funnel Display font](https://fonts.google.com/specimen/Funnel+Display). */
    FunnelDisplay(
        protoValue = TypefaceSettingProto.FunnelDisplay,
        category = Category.Default
    ),

    /**
     * Use the system's default font.
     * This is typically [Roboto](https://fonts.google.com/specimen/Roboto).
     */
    SystemDefault(
        protoValue = TypefaceSettingProto.SystemDefault,
        category = Category.System
    ),

    /** Use the system's sans-serif font. */
    SystemSansSerif(
        protoValue = TypefaceSettingProto.SystemSansSerif,
        category = Category.System
    ),

    /** Use the system's serif font. */
    SystemSerif(
        protoValue = TypefaceSettingProto.SystemSerif,
        category = Category.System
    ),

    /** Use the system's monospace font. */
    SystemMonospace(
        protoValue = TypefaceSettingProto.SystemMonospace,
        category = Category.System
    );

    enum class Category {
        /** Denotes the default fonts to use. ([Baloo2], [FunnelDisplay]) */
        Default,

        /** Denotes the available system fonts to use. */
        System
    }

    companion object {
        fun fromProto(proto: TypefaceSettingProto): TypefaceSetting? =
            entries.find { it.protoValue == proto }
    }
}

@get:StringRes
val TypefaceSetting.labelResource: Int
    get() = when (this) {
        TypefaceSetting.Baloo2 -> R.string.pref_font_style_entry_baloo_2
        TypefaceSetting.FunnelDisplay -> R.string.pref_font_style_entry_funnel_display
        TypefaceSetting.SystemDefault -> R.string.pref_font_style_entry_system_default
        TypefaceSetting.SystemSansSerif -> R.string.pref_font_style_entry_system_sans_serif
        TypefaceSetting.SystemSerif -> R.string.pref_font_style_entry_system_serif
        TypefaceSetting.SystemMonospace -> R.string.pref_font_style_entry_system_monospace
    }
