package com.hninhnin.wai.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class CartProduct(val id: Int,
    val cartId: Int,
    val name: String,
    val price: Int,
    val image: String,
    val description: String,
    val count: Int
    ) : Parcelable