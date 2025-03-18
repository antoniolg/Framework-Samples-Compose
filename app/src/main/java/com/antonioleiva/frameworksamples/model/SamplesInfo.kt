package com.antonioleiva.frameworksamples.model

import androidx.annotation.StringRes

data class SamplesInfo(
    @StringRes val title: Int,
    val samples: List<Sample>
)