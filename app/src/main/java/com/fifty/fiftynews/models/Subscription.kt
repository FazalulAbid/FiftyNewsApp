package com.fifty.fiftynews.models

import com.google.type.Date
import java.time.LocalDate

data class Subscription(
    val id: String? = null,
    val uid: String? = null,
    val planId: String? = null,
    val startsDate: String? = null,
    val endsDate: String? = null
)
