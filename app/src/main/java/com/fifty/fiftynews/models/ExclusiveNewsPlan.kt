package com.fifty.fiftynews.models

data class ExclusiveNewsPlan(
    val id: String? = null,
    val title: String? = null,
    val subTitle: String? = null,
    val price: Double? = null,
    val numberOfMonths: Int? = null,
    val itemSelected: Boolean? = false,
    val isActive: Boolean? = false
)