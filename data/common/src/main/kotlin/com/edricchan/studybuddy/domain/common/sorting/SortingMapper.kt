package com.edricchan.studybuddy.domain.common.sorting

import com.edricchan.studybuddy.data.common.proto.SortDirection as ProtoSortDirection

fun ProtoSortDirection.toDomain(): SortDirection = when (this) {
    ProtoSortDirection.Descending -> SortDirection.Descending
    ProtoSortDirection.Ascending -> SortDirection.Ascending
}

fun SortDirection.toProto(): ProtoSortDirection = when (this) {
    SortDirection.Ascending -> ProtoSortDirection.Ascending
    SortDirection.Descending -> ProtoSortDirection.Descending
}
