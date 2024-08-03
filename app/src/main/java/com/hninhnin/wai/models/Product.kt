package com.hninhnin.wai.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Product(
    val id: Int,
    val catId: Int,
    val name: String,
    val price: Int,
    val image: String,
    val description: String
) : Parcelable