package com.edricchan.studybuddy.core.metadata.updates.source.store

import com.edricchan.studybuddy.core.settings.updates.proto.UpdateMetadata
import com.edricchan.studybuddy.utils.wire.datastore.WireSerializer

object UpdateMetadataSerializer : WireSerializer<UpdateMetadata>(
    defaultValue = UpdateMetadata(),
    adapter = UpdateMetadata.ADAPTER
)
