package com.edricchan.studybuddy.core.settings.general.locale

import com.edricchan.studybuddy.core.settings.general.proto.LocaleSettings as LocaleSettingsProto

sealed interface AppLanguage {
    /** Follow the system setting. */
    data object System : AppLanguage

    /**
     * Use a custom language code.
     * @property langCode The language code in ISO639 form.
     */
    data class Custom(val langCode: String) : AppLanguage

    companion object {
        fun fromProto(localeSettings: LocaleSettingsProto): AppLanguage? {
            if (localeSettings.use_system_lang == true) return System
            return localeSettings.lang_code?.let { Custom(it) }
        }
    }
}
