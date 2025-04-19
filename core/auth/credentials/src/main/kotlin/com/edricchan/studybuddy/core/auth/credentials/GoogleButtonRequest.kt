package com.edricchan.studybuddy.core.auth.credentials

import android.content.Context
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import com.edricchan.studybuddy.core.auth.gms.R as GmsR

@OptIn(ExperimentalUuidApi::class)
val Context.googleBtnOption: GetSignInWithGoogleOption
    get() = GetSignInWithGoogleOption(
        serverClientId = getString(GmsR.string.default_web_client_id),
        hostedDomainFilter = null,
        nonce = Uuid.random().toString()
    )
